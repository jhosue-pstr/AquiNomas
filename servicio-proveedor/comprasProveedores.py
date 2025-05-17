import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False

def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="SERVICIO-PROVEEDOR",  # debe coincidir con el nombre registrado en Eureka y Config Server
            instance_id=f"servicio-proveedor-{puerto}",
            health_check_url=f"http://localhost:{puerto}/health",
            home_page_url=f"http://localhost:{puerto}",
            instance_port=puerto,
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka en el puerto {puerto}")
    else:
        print("Eureka client ya está registrado.")
 
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
        # Extraer host, puerto y base de datos desde la URL jdbc:mysql://host:port/dbname
        host_port = db_url.split("://")[1].split("/")[0]
        if ":" in host_port:
            host, port = host_port.split(":")
            port = int(port)
        else:
            host = host_port
            port = 3306  # puerto por defecto mysql si no especificado
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



def crear_compra_proveedor(proveedor_id, total):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("INSERT INTO compra_proveedor (proveedor_id, total) VALUES (%s, %s)",
                   (proveedor_id, total))
    conexion.commit()
    conexion.close()

def obtener_compras_proveedor():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM compra_proveedor")
    compras = cursor.fetchall()
    for compra in compras:
        cursor.execute("SELECT * FROM proveedor WHERE id = %s", (compra['proveedor_id'],))
        proveedor = cursor.fetchone()
        compra['proveedor'] = proveedor

    conexion.close()
    return compras


def obtener_compra_proveedor_por_id(compra_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM compra_proveedor WHERE id = %s", (compra_id,))
    compra = cursor.fetchone()
    if compra:
        cursor.execute("SELECT * FROM proveedor WHERE id = %s", (compra['proveedor_id'],))
        proveedor = cursor.fetchone()
        compra['proveedor'] = proveedor
    conexion.close()
    return compra


def actualizar_compra_proveedor(compra_id, proveedor_id, total):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE compra_proveedor SET proveedor_id = %s, total = %s
        WHERE id = %s
    """, (proveedor_id, total, compra_id))
    conexion.commit()
    conexion.close()

def eliminar_compra_proveedor(compra_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM compra_proveedor WHERE id = %s", (compra_id,))
    conexion.commit()
    conexion.close()
