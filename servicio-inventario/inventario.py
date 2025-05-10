import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False

# Registrar el servicio en Eureka
def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",  # URL de Eureka
            app_name="SERVICIO-PRODUCTO",  # Nombre del servicio registrado
            instance_id=f"servicio-producto-{puerto}",  # ID único para la instancia
            health_check_url=f"http://localhost:{puerto}/health",  # URL para chequear la salud
            home_page_url=f"http://localhost:{puerto}",  # URL de la página principal
            instance_port=puerto,  # Puerto donde corre el servicio
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

# Obtener la conexión a la base de datos
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

