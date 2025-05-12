# categorias.py

import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False

def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",  
            app_name="SERVICIO-PRODUCTO", 
            instance_id=f"servicio-producto-{puerto}",  
            health_check_url=f"http://localhost:{puerto}/health",  
            home_page_url=f"http://localhost:{puerto}",  
            instance_port=puerto,  
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka en el puerto {puerto}")
    else:
        print("Eureka client ya está registrado.")

# Cargar la configuración desde el servidor de configuración
def cargar_configuracion(app_name, profile="default", config_server_url="http://localhost:7070"):
    url = f"{config_server_url}/{app_name}/{profile}"
    response = requests.get(url, auth=("root", "123456"))

    if response.status_code == 200:
        config = response.json()
        print("CONFIG OBTENIDA:", config)
        propiedades = config.get("propertySources", [])
        
        resultado = {}
        for fuente in propiedades:
            resultado.update(fuente["source"])
        
        return resultado
    else:
        print("Error al obtener la configuración del servidor:", response.status_code)
        return {}

def obtener_conexion():
    config = cargar_configuracion("servicio-producto")

    db_url = config.get("spring.jpa.datasource.url", "")

    if not db_url.startswith("jdbc:mysql://"):
        raise ValueError("La URL de la base de datos no está configurada correctamente.")
    
    try:
        host = db_url.split("://")[1].split("/")[0].split(":")[0]
        port = db_url.split("://")[1].split("/")[0].split(":")[1]
        database = db_url.split("/")[-1]
    except IndexError:
        raise ValueError("Error al analizar la URL de la base de datos.")
    
    user = config.get("spring.jpa.datasource.username", "root")
    password = config.get("spring.jpa.datasource.password", "")

    return mysql.connector.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    )





def crear_movimiento_inventario(producto_id, tipo_movimiento, cantidad):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        INSERT INTO movimiento_inventario (producto_id, tipo_movimiento, cantidad)
        VALUES (%s, %s, %s)
    """, (producto_id, tipo_movimiento, cantidad))
    conexion.commit()
    conexion.close()

def obtener_movimientos_inventario():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM movimiento_inventario")
    movimientos = cursor.fetchall()
    conexion.close()
    return movimientos

def obtener_movimiento_inventario_por_id(movimiento_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM movimiento_inventario WHERE id = %s", (movimiento_id,))
    movimiento = cursor.fetchone()
    conexion.close()
    return movimiento

def actualizar_movimiento_inventario(movimiento_id, producto_id, tipo_movimiento, cantidad):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE movimiento_inventario
        SET producto_id = %s, tipo_movimiento = %s, cantidad = %s
        WHERE id = %s
    """, (producto_id, tipo_movimiento, cantidad, movimiento_id))
    conexion.commit()
    conexion.close()

def eliminar_movimiento_inventario(movimiento_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM movimiento_inventario WHERE id = %s", (movimiento_id,))
    conexion.commit()
    conexion.close()
