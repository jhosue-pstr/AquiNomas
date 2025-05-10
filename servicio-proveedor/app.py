from flask import Flask, request, jsonify
import proveedores

app = Flask(__name__)

@app.route('/health')
def health():
    return "OK", 200
@app.route('/proveedores/test')
def test():
    return "Hola desde Flask proveedor"

@app.route('/proveedores', methods=['POST'])
def crear():
    data = request.json
    proveedores.crear_proveedor(data['nombre'], data['telefono'], data['direccion'], data['email'])
    return jsonify({"mensaje": "Proveedor creado exitosamente"}), 201

@app.route('/proveedores', methods=['GET'])
def listar():
    lista = proveedores.obtener_proveedores()
    return jsonify(lista)

@app.route('/proveedores/<int:proveedor_id>', methods=['GET'])
def obtener(proveedor_id):
    proveedor = proveedores.obtener_proveedor_por_id(proveedor_id)
    if proveedor:
        return jsonify(proveedor)
    return jsonify({"error": "Proveedor no encontrado"}), 404

@app.route('/proveedores/<int:proveedor_id>', methods=['PUT'])
def actualizar(proveedor_id):
    data = request.json
    proveedores.actualizar_proveedor(proveedor_id, data['nombre'], data['telefono'], data['direccion'], data['email'])
    return jsonify({"mensaje": "Proveedor actualizado correctamente"})

@app.route('/proveedores/<int:proveedor_id>', methods=['DELETE'])
def eliminar(proveedor_id):
    proveedores.eliminar_proveedor(proveedor_id)
    return jsonify({"mensaje": "Proveedor eliminado correctamente"})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)
