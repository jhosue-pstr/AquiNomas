
import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False

def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",  
            app_name="SERVICIO-INVENTARIO", 
            instance_id=f"servicio-inventario-{puerto}",  
            health_check_url=f"http://localhost:{puerto}/health",  
            home_page_url=f"http://localhost:{puerto}",  
            instance_port=puerto,  
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka en el puerto {puerto}")
    else:
        print("Eureka client ya está registrado.")

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
    config = cargar_configuracion("servicio-inventario")

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





def crear_ajuste_inventario(producto_id, cantidad, motivo, tipo_ajuste):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        INSERT INTO ajuste_inventario (producto_id, cantidad, motivo, tipo_ajuste)
        VALUES (%s, %s, %s, %s)
    """, (producto_id, cantidad, motivo, tipo_ajuste))
    conexion.commit()
    conexion.close()

def obtener_ajustes_inventario():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM ajuste_inventario")
    ajustes = cursor.fetchall()
    conexion.close()
    return ajustes

def obtener_ajuste_inventario_por_id(ajuste_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM ajuste_inventario WHERE id = %s", (ajuste_id,))
    ajuste = cursor.fetchone()
    conexion.close()
    return ajuste

def actualizar_ajuste_inventario(ajuste_id, producto_id, cantidad, motivo, tipo_ajuste):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE ajuste_inventario
        SET producto_id = %s, cantidad = %s, motivo = %s, tipo_ajuste = %s
        WHERE id = %s
    """, (producto_id, cantidad, motivo, tipo_ajuste, ajuste_id))
    conexion.commit()
    conexion.close()

def eliminar_ajuste_inventario(ajuste_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM ajuste_inventario WHERE id = %s", (ajuste_id,))
    conexion.commit()
    conexion.close()
