from flask import Flask, request, jsonify
import proveedores
import productosProveedores
import comprasProveedores
import detalleComprasProveedores
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

#===============RUTAS====================#
#==========Proveedores===================#
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

#======================= compras Proveedores =============+++#
@app.route('/compras-proveedores', methods=['POST'])
def crear_compra():
    data = request.json
    comprasProveedores.crear_compra_proveedor(data['proveedor_id'], data['total'])
    return jsonify({"mensaje": "Compra registrada exitosamente"}), 201

@app.route('/compras-proveedores', methods=['GET'])
def listar_compras():
    lista = comprasProveedores.obtener_compras_proveedor()
    return jsonify(lista)

@app.route('/compras-proveedores/<int:compra_id>', methods=['PUT'])
def actualizar_compra(compra_id):
    data = request.json
    comprasProveedores.actualizar_compra_proveedor(compra_id, data['proveedor_id'], data['total'])
    return jsonify({"mensaje": "Compra actualizada correctamente"})

@app.route('/compras-proveedores/<int:compra_id>', methods=['DELETE'])
def eliminar_compra(compra_id):
    comprasProveedores.eliminar_compra_proveedor(compra_id)
    return jsonify({"mensaje": "Compra eliminada correctamente"})

@app.route('/compras-proveedores/<int:compra_id>', methods=['GET'])
def obtener_compra(compra_id):
    try:
        return jsonify(call_with_resilience_compra(compra_id))
    except Exception:
        return fallback_compra(compra_id)

@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience_compra(compra_id):
    compra = comprasProveedores.obtener_compra_proveedor_por_id(compra_id)
    if compra:
        return compra
    raise Exception("Compra no encontrada")

def fallback_compra(compra_id):
    return jsonify({
        "id": compra_id,
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200
#============================detalle de compra ==================#
@app.route('/detalle_compras', methods=['POST'])
def crear_detalle_compra():
    data = request.json
    detalleComprasProveedores.crear_detalle_compra(data['compra_id'], data['producto_id'], data['cantidad'], data['precio_unitario'], data['total'])
    return jsonify({"mensaje": "Detalle de compra creado exitosamente"}), 201

@app.route('/detalle_compras', methods=['GET'])
def listar_detalles_compras():
    lista = detalleComprasProveedores.obtener_detalles_compra()
    return jsonify(lista)

@app.route('/detalle_compras/<int:detalle_id>', methods=['GET'])
def obtener_detalle_compra(detalle_id):
    try:
        return jsonify(call_with_resilience_detalle(detalle_id))
    except Exception:
        return fallback_detalle_compra(detalle_id)

@app.route('/detalle_compras/<int:detalle_id>', methods=['PUT'])
def actualizar_detalle_compra(detalle_id):
    data = request.json
    detalleComprasProveedores.actualizar_detalle_compra(detalle_id, data['compra_id'], data['producto_id'], data['cantidad'], data['precio_unitario'], data['total'])
    return jsonify({"mensaje": "Detalle de compra actualizado correctamente"})

@app.route('/detalle_compras/<int:detalle_id>', methods=['DELETE'])
def eliminar_detalle_compra(detalle_id):
    detalleComprasProveedores.eliminar_detalle_compra(detalle_id)
    return jsonify({"mensaje": "Detalle de compra eliminado correctamente"})


@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience_detalle(detalle_id):
    detalle = detalleComprasProveedores.obtener_detalle_compra_por_id(detalle_id)
    if detalle:
        return detalle
    raise Exception("Detalle de compra no encontrado")  

def fallback_detalle_compra(detalle_id):
    return jsonify({
        "id": detalle_id,
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200





if __name__ == "__main__":
    app.run(port=PORT, debug=False)
