import mysql.connector
import requests

def cargar_configuracion(app_name, profile="default", config_server_url="http://localhost:7070"):
    url = f"{config_server_url}/{app_name}/{profile}"
    response = requests.get(url)
    
    if response.status_code == 200:
        config = response.json()
        propiedades = config.get("propertySources", [])
        
        resultado = {}
        for fuente in propiedades:
            resultado.update(fuente["source"])  # fusiona todas las configuraciones
        
        return resultado
    else:
        print("Error al obtener la configuración del servidor:", response.status_code)
        return {}
    
    
def obtener_conexion():
    config = cargar_configuracion("servicio-proveedor")
    
    host = config.get("spring.datasource.url", "").split("://")[1].split("/")[0].split(":")[0]
    port = config.get("spring.datasource.url", "").split("://")[1].split("/")[0].split(":")[1]
    database = config.get("spring.datasource.url", "").split("/")[-1]
    user = config.get("spring.datasource.username", "root")
    password = config.get("spring.datasource.password", "")

    return mysql.connector.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    )



def crear_proveedor(nombre, telefono, direccion, email):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("INSERT INTO proveedor (nombre, telefono, direccion, email) VALUES (%s, %s, %s, %s)",
                   (nombre, telefono, direccion, email))
    conexion.commit()
    conexion.close()

def obtener_proveedores():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM proveedor")
    proveedores = cursor.fetchall()
    conexion.close()
    return proveedores

def obtener_proveedor_por_id(proveedor_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM proveedor WHERE id = %s", (proveedor_id,))
    proveedor = cursor.fetchone()
    conexion.close()
    return proveedor

def actualizar_proveedor(proveedor_id, nombre, telefono, direccion, email):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE proveedor SET nombre = %s, telefono = %s, direccion = %s, email = %s
        WHERE id = %s
    """, (nombre, telefono, direccion, email, proveedor_id))
    conexion.commit()
    conexion.close()

def eliminar_proveedor(proveedor_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM proveedor WHERE id = %s", (proveedor_id,))
    conexion.commit()
    conexion.close()
