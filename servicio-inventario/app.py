from flask import Flask, request, jsonify
import inventario
import ajustesInventario
import pybreaker
import os
import random
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)
PORT = int(os.environ.get("PORT", random.randint(5001, 5999)))

inventario.registrar_en_eureka(PORT)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200

#=============RUTAS-==================#
#================inventario=====================+#

@app.route('/inventario', methods=['POST'])
def crear_inventario():
    data = request.json
    inventario.crear_inventario(data['producto_id'], data['cantidad_disponible'], data['fecha_vencimiento'])
    return jsonify({"mensaje": "Inventario creado exitosamente"}), 201

@app.route('/inventario', methods=['GET'])
def listar_inventarios():
    lista = inventario.obtener_inventarios()
    return jsonify(lista)

@app.route('/inventario/<int:producto_id>', methods=['GET'])
def obtener_inventario(producto_id):
    try:
        return jsonify(call_with_resilience_inventario(producto_id))
    except Exception:
        return fallback_inventario(producto_id)

@app.route('/inventario/<int:producto_id>', methods=['PUT'])
def actualizar_inventario(producto_id):
    data = request.json
    inventario.actualizar_inventario(producto_id, data['cantidad_disponible'], data['fecha_vencimiento'])
    return jsonify({"mensaje": "Inventario actualizado correctamente"})

@app.route('/inventario/<int:producto_id>', methods=['DELETE'])
def eliminar_inventario(producto_id):
    inventario.eliminar_inventario(producto_id)
    return jsonify({"mensaje": "Inventario eliminado correctamente"})


@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience_inventario(producto_id):
    inventario = inventario.obtener_inventario_por_producto(producto_id)
    if inventario:
        return inventario
    raise Exception("Inventario no encontrado")  

def fallback_inventario(producto_id):
    return jsonify({
        "producto_id": producto_id,
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200




@app.route('/alertas_stock', methods=['GET'])
def alertas_stock_bajo():
    alertas = inventario.obtener_alertas_stock_bajo()
    return jsonify(alertas)




#================ajustes de inventario=====================+#
@app.route('/ajustes_inventario', methods=['POST'])
def crear_ajuste_inventario():
    data = request.json
    ajustesInventario.crear_ajuste_inventario(data['producto_id'], data['cantidad'], data['motivo'], data['tipo_ajuste'])
    return jsonify({"mensaje": "Ajuste de inventario creado exitosamente"}), 201

@app.route('/ajustes_inventario', methods=['GET'])
def listar_ajustes_inventario():
    lista = ajustesInventario.obtener_ajustes_inventario()
    return jsonify(lista)

@app.route('/ajustes_inventario/<int:ajuste_id>', methods=['GET'])
def obtener_ajuste_inventario(ajuste_id):
    try:
        return jsonify(call_with_resilience_ajustes_inventario(ajuste_id))
    except Exception:
        return fallback_ajustes_inventario(ajuste_id)

@app.route('/ajustes_inventario/<int:ajuste_id>', methods=['PUT'])
def actualizar_ajuste_inventario(ajuste_id):
    data = request.json
    ajustesInventario.actualizar_ajuste_inventario(ajuste_id, data['producto_id'], data['cantidad'], data['motivo'], data['tipo_ajuste'])
    return jsonify({"mensaje": "Ajuste de inventario actualizado correctamente"})

@app.route('/ajustes_inventario/<int:ajuste_id>', methods=['DELETE'])
def eliminar_ajuste_inventario(ajuste_id):
    ajustesInventario.eliminar_ajuste_inventario(ajuste_id)
    return jsonify({"mensaje": "Ajuste de inventario eliminado correctamente"})


@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience_ajustes_inventario(ajuste_id):
    ajuste = ajustesInventario.obtener_ajuste_inventario_por_id(ajuste_id)
    if ajuste:
        return ajuste
    raise Exception("Ajuste de inventario no encontrado")  

def fallback_ajustes_inventario(ajuste_id):
    return jsonify({
        "id": ajuste_id,
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200
#====================== movimientos inventario ============++#



import movimientosInventario

@app.route('/movimientos_inventario', methods=['POST'])
def crear_movimiento_inventario():
    data = request.json
    movimientosInventario.crear_movimiento_inventario(data['producto_id'], data['tipo_movimiento'], data['cantidad'])
    return jsonify({"mensaje": "Movimiento de inventario creado exitosamente"}), 201

@app.route('/movimientos_inventario', methods=['GET'])
def listar_movimientos_inventario():
    lista = movimientosInventario.obtener_movimientos_inventario()
    return jsonify(lista)

@app.route('/movimientos_inventario/<int:movimiento_id>', methods=['GET'])
def obtener_movimiento_inventario(movimiento_id):
    try:
        return jsonify(call_with_resilience_movimientos_inventario(movimiento_id))
    except Exception:
        return fallback_movimientos_inventario(movimiento_id)

@app.route('/movimientos_inventario/<int:movimiento_id>', methods=['PUT'])
def actualizar_movimiento_inventario(movimiento_id):
    data = request.json
    movimientosInventario.actualizar_movimiento_inventario(movimiento_id, data['producto_id'], data['tipo_movimiento'], data['cantidad'])
    return jsonify({"mensaje": "Movimiento de inventario actualizado correctamente"})

@app.route('/movimientos_inventario/<int:movimiento_id>', methods=['DELETE'])
def eliminar_movimiento_inventario(movimiento_id):
    movimientosInventario.eliminar_movimiento_inventario(movimiento_id)
    return jsonify({"mensaje": "Movimiento de inventario eliminado correctamente"})


@retry(stop=stop_after_attempt(2), wait=wait_fixed(1))
@breaker
def call_with_resilience_movimientos_inventario(movimiento_id):
    movimiento = movimientosInventario.obtener_movimiento_inventario_por_id(movimiento_id)
    if movimiento:
        return movimiento
    raise Exception("Movimiento de inventario no encontrado")  

def fallback_movimientos_inventario(movimiento_id):
    return jsonify({
        "id": movimiento_id,
        "mensaje": "Servicio no disponible - fallback activado"
    }), 200




if __name__ == "__main__":
    app.run(port=PORT, debug=False)
