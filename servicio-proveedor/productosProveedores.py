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
    config = cargar_configuracion("servicio-proveedor")

    db_url = config.get("spring.jpa.datasource.url", "")

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


def crear_producto_proveedor(proveedor_id, producto_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("INSERT INTO producto_proveedor (proveedor_id, producto_id) VALUES (%s, %s)",
                   (proveedor_id, producto_id))
    conexion.commit()
    conexion.close()


def obtener_productos_proveedor():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM producto_proveedor")
    relaciones = cursor.fetchall()
    conexion.close()
    return relaciones


def obtener_relacion_por_id(proveedor_id, producto_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM producto_proveedor WHERE proveedor_id = %s AND producto_id = %s",
                   (proveedor_id, producto_id))
    relacion = cursor.fetchone()
    conexion.close()
    return relacion


def actualizar_producto_proveedor(proveedor_id_actual, producto_id_actual, nuevo_proveedor_id, nuevo_producto_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE producto_proveedor 
        SET proveedor_id = %s, producto_id = %s
        WHERE proveedor_id = %s AND producto_id = %s
    """, (nuevo_proveedor_id, nuevo_producto_id, proveedor_id_actual, producto_id_actual))
    conexion.commit()
    conexion.close()


def eliminar_producto_proveedor(proveedor_id, producto_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM producto_proveedor WHERE proveedor_id = %s AND producto_id = %s",
                   (proveedor_id, producto_id))
    conexion.commit()
    conexion.close()
