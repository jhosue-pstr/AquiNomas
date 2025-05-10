from flask import Flask, request, jsonify
import proveedores
import pybreaker
import os
import random
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)
PORT = int(os.environ.get("PORT", random.randint(5001, 5999)))

proveedores.registrar_en_eureka(PORT)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200

@app.route('/proveedores', methods=['POST'])
def crear():
    data = request.json
    proveedores.crear_proveedor(data['nombre'], data['telefono'], data['direccion'], data['email'])
    return jsonify({"mensaje": "Proveedor creado exitosamente"}), 201

@app.route('/proveedores', methods=['GET'])
def listar():
    lista = proveedores.obtener_proveedores()
    return jsonify(lista)

@app.route('/proveedores/<int:proveedor_id>', methods=['PUT'])
def actualizar(proveedor_id):
    data = request.json
    proveedores.actualizar_proveedor(proveedor_id, data['nombre'], data['telefono'], data['direccion'], data['email'])
    return jsonify({"mensaje": "Proveedor actualizado correctamente"})

@app.route('/proveedores/<int:proveedor_id>', methods=['DELETE'])
def eliminar(proveedor_id):
    proveedores.eliminar_proveedor(proveedor_id)
    return jsonify({"mensaje": "Proveedor eliminado correctamente"})



#circuit braker 

@app.route('/proveedores/<int:proveedor_id>', methods=['GET'])
def obtener(proveedor_id):
    try:
        return jsonify(call_with_resilience(proveedor_id))
    except Exception:
        return fallback_proveedor(proveedor_id)

@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience(proveedor_id):
    proveedor = proveedores.obtener_proveedor_por_id(proveedor_id)
    if proveedor:
        return proveedor
    raise Exception("Proveedor no encontrado")  
def fallback_proveedor(proveedor_id):
    return jsonify({
        "id": proveedor_id,
        "nombre": "Desconocido",
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200




if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)
