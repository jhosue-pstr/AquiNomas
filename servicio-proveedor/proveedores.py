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
