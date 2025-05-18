import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False

def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1" 

        import py_eureka_client.eureka_client as eureka_client
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="SERVICIO-PROVEEDOR",
            instance_id=f"servicio-proveedor-{puerto}",
            health_check_url=f"http://{ip}:{puerto}/health",
            home_page_url=f"http://{ip}:{puerto}",
            instance_host=ip,
            instance_port=puerto,
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka correctamente: http://{ip}:{puerto}")




def cargar_configuracion(app_name="servicio-proveedor", profile="default", config_server_url="http://localhost:7070"):
    url = f"{config_server_url}/{app_name}/{profile}"
    response = requests.get(url, auth=("root", "123456"))
    if response.status_code == 200:
        config = response.json()
        print("CONFIG OBTENIDA:", config)
        propiedades = config.get("propertySources", [])
        resultado = {}
        for fuente in propiedades:
            resultado.update(fuente.get("source", {}))
        return resultado
    else:
        print("Error al obtener la configuración del servidor:", response.status_code)
        return {}

def obtener_conexion():
    config = cargar_configuracion("servicio-proveedor")

    db_url = config.get("spring.jpa.datasource.url", "")

    if not db_url.startswith("jdbc:mysql://"):
        raise ValueError("La URL de la base de datos no está configurada correctamente.")
    
    try:
        host_port = db_url.split("://")[1].split("/")[0]
        if ":" in host_port:
            host, port = host_port.split(":")
            port = int(port)
        else:
            host = host_port
            port = 3306
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





import requests
import xml.etree.ElementTree as ET

URL_SERVICIO_PRODUCTO = None

def obtener_url_servicio_producto():
    global URL_SERVICIO_PRODUCTO
    try:
        response = requests.get("http://localhost:8090/eureka/apps/SERVICIO-PRODUCTO")
        if response.status_code == 200:
            root = ET.fromstring(response.content)
            instance = root.find('instance')
            if instance is not None:
                host = instance.find('hostName').text
                port = instance.find('port').text
                URL_SERVICIO_PRODUCTO = f"http://{host}:{port}"
                print(f"URL servicio-producto actualizada: {URL_SERVICIO_PRODUCTO}")
                return URL_SERVICIO_PRODUCTO
            else:
                print("No se encontró la instancia en la respuesta de Eureka.")
                URL_SERVICIO_PRODUCTO = None
                return None
        else:
            print(f"Error al consultar Eureka: status {response.status_code}")
            URL_SERVICIO_PRODUCTO = None
            return None
    except Exception as e:
        print(f"Error al obtener URL del servicio producto: {e}")
        URL_SERVICIO_PRODUCTO = None
        return None

# Inicializar al cargar módulo
obtener_url_servicio_producto()

def obtener_producto_por_id(producto_id):
    global URL_SERVICIO_PRODUCTO
    if not URL_SERVICIO_PRODUCTO:
        if not obtener_url_servicio_producto():
            return {"error": "No se pudo resolver la URL del servicio-producto"}

    try:
        response = requests.get(f"{URL_SERVICIO_PRODUCTO}/productos/{producto_id}")
        if response.status_code == 200:
            return response.json()
        else:
            return {"error": "Producto no encontrado"}
    except Exception as e:
        print(f"Error al contactar con servicio-producto: {str(e)}")
        obtener_url_servicio_producto()
        return {"error": "Error al contactar con servicio-producto"}








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

    for rel in relaciones:
        producto = obtener_producto_por_id(rel["producto_id"])
        rel["producto"] = producto

        cursor.execute("SELECT * FROM proveedor WHERE id = %s", (rel["proveedor_id"],))
        proveedor = cursor.fetchone()
        rel["proveedor"] = proveedor

    conexion.close()
    return relaciones

def obtener_relaciones_por_proveedor_id(proveedor_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM producto_proveedor WHERE proveedor_id = %s", (proveedor_id,))
    relaciones = cursor.fetchall()

    for rel in relaciones:
        # Obtener datos del producto desde el servicio externo
        producto = obtener_producto_por_id(rel["producto_id"])
        rel["producto"] = producto

        # Obtener datos del proveedor desde la misma base de datos
        cursor.execute("SELECT * FROM proveedor WHERE id = %s", (rel["proveedor_id"],))
        proveedor = cursor.fetchone()
        rel["proveedor"] = proveedor

    conexion.close()
    return relaciones


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
