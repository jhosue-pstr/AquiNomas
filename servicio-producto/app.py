from flask import Flask, request, jsonify
import productos
import categorias
import pybreaker
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200



@app.route('/productos', methods=['POST'])
def crear_producto():
    data = request.json
    nombre = data.get('nombre')
    categoria_id = data.get('categoria_id')
    descripcion = data.get('descripcion')
    precio = data.get('precio')
    stock = data.get('stock')
    productos.crear_producto(nombre, categoria_id, descripcion, precio, stock)
    return jsonify({"mensaje": "Producto creado"}), 201

@app.route('/productos', methods=['GET'])
def listar_productos():
    lista = productos.obtener_productos()
    return jsonify(lista)

@app.route('/productos/<int:producto_id>', methods=['GET'])
def obtener_producto(producto_id):
    producto = productos.obtener_producto_por_id(producto_id)
    if producto:
        return jsonify(producto)
    return jsonify({"mensaje": "Producto no encontrado"}), 404

@app.route('/productos/<int:producto_id>', methods=['PUT'])
def actualizar_producto(producto_id):
    data = request.json
    nombre = data.get('nombre')
    categoria_id = data.get('categoria_id')
    descripcion = data.get('descripcion')
    precio = data.get('precio')
    stock = data.get('stock')
    productos.actualizar_producto(producto_id, nombre, categoria_id, descripcion, precio, stock)
    return jsonify({"mensaje": "Producto actualizado"})

@app.route('/productos/<int:producto_id>', methods=['DELETE'])
def eliminar_producto(producto_id):
    productos.eliminar_producto(producto_id)
    return jsonify({"mensaje": "Producto eliminado"})

#==============categoria============================+#
@app.route('/categorias', methods=['POST'])
def crear_categoria():
    data = request.json
    nombre = data.get('nombre')
    categorias.crear_categoria(nombre)
    return jsonify({"mensaje": "Categoría creada"}), 201

# Ruta para obtener todas las categorías
@app.route('/categorias', methods=['GET'])
def listar_categorias():
    lista = categorias.obtener_categorias()
    return jsonify(lista)

# Ruta para obtener una categoría por ID
@app.route('/categorias/<int:categoria_id>', methods=['GET'])
def obtener_categoria(categoria_id):
    categoria = categorias.obtener_categoria_por_id(categoria_id)
    if categoria:
        return jsonify(categoria)
    return jsonify({"mensaje": "Categoría no encontrada"}), 404



# Ruta para actualizar una categoría por ID
@app.route('/categorias/<int:categoria_id>', methods=['PUT'])
def actualizar_categoria(categoria_id):
    data = request.json
    nombre = data.get('nombre')
    categorias.actualizar_categoria(categoria_id, nombre)
    return jsonify({"mensaje": "Categoría actualizada"})

# Ruta para eliminar una categoría por ID
@app.route('/categorias/<int:categoria_id>', methods=['DELETE'])
def eliminar_categoria(categoria_id):
    categorias.eliminar_categoria(categoria_id)
    return jsonify({"mensaje": "Categoría eliminada"})



if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5002)
