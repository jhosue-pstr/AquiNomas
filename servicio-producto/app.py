from flask import Flask, request, jsonify
import productos
import pybreaker
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200



@app.route('/productos', methods=['GET'])
def listar():
    lista = productos.obtener_productos()
    return jsonify(lista)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5002)
