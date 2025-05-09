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
    # Cargar configuración desde el servidor
    config = cargar_configuracion("servicio-proveedor")

    # Verificar si la URL de la base de datos existe y tiene el formato esperado
    db_url = config.get("spring.datasource.url", "")
    
    if not db_url:
        raise ValueError("La URL de la base de datos no está configurada correctamente.")
    
    # Intentar dividir la URL y obtener los valores de host, puerto y base de datos
    try:
        # Si el formato es 'jdbc:mysql://localhost:3306/proveedor_db', lo dividimos
        host = db_url.split("://")[1].split("/")[0].split(":")[0]
        port = db_url.split("://")[1].split("/")[0].split(":")[1]
        database = db_url.split("/")[-1]
    except IndexError:
        raise ValueError("Error al analizar la URL de la base de datos. Asegúrate de que tenga el formato correcto.")
    
    # Obtener el usuario y la contraseña de la configuración
    user = config.get("spring.datasource.username", "root")
    password = config.get("spring.datasource.password", "")

    # Conectar a la base de datos MySQL
    return mysql.connector.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    )

# Funciones CRUD para proveedores
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
