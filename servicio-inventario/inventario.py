
import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
import socket
eureka_registered = False




def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1"  # o usa: socket.gethostbyname(socket.gethostname())

        import py_eureka_client.eureka_client as eureka_client
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="SERVICIO-INVENTARIO",
            instance_id=f"servicio-inventario-{puerto}",
            health_check_url=f"http://{ip}:{puerto}/health",
            home_page_url=f"http://{ip}:{puerto}",
            instance_host=ip,
            instance_port=puerto,
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka correctamente: http://{ip}:{puerto}")


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
        # Intenta actualizar URL
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





def crear_inventario(producto_id, cantidad_disponible, fecha_vencimiento):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        INSERT INTO inventario (producto_id, cantidad_disponible, fecha_vencimiento)
        VALUES (%s, %s, %s)
    """, (producto_id, cantidad_disponible, fecha_vencimiento))
    conexion.commit()
    conexion.close()

def obtener_inventarios():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM inventario")
    inventarios = cursor.fetchall()
    conexion.close()
    
    for inv in inventarios:
        producto = obtener_producto_por_id(inv["producto_id"])  # Llama a servicio-producto
        inv["producto"] = producto

    return inventarios

def obtener_inventario_por_id(inventario_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM inventario WHERE id = %s", (inventario_id,))
    inventario = cursor.fetchone()
    conexion.close()

    if inventario:
        producto = obtener_producto_por_id(inventario["producto_id"])  # Llama a servicio-producto
        inventario["producto"] = producto

    return inventario


def actualizar_inventario_por_id(inventario_id, cantidad_disponible, fecha_vencimiento):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE inventario
        SET cantidad_disponible = %s, fecha_vencimiento = %s
        WHERE id = %s
    """, (cantidad_disponible, fecha_vencimiento, inventario_id))
    conexion.commit()
    conexion.close()

def eliminar_inventario_por_id(inventario_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM inventario WHERE id = %s", (inventario_id,))
    conexion.commit()
    conexion.close()




def obtener_alertas_stock_bajo():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("""
        SELECT * FROM inventario 
        WHERE cantidad_disponible < stock_minimo
    """)
    alertas = cursor.fetchall()
    conexion.close()

    for alerta in alertas:
        producto = obtener_producto_por_id(alerta["producto_id"])
        alerta["producto"] = producto
        alerta["alerta"] = "Stock bajo - Comprar"

    return alertas

