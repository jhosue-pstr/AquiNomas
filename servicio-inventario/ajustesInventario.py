
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







def crear_ajuste_inventario(producto_id, cantidad, motivo, tipo_ajuste):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        INSERT INTO ajuste_inventario (producto_id, cantidad, motivo, tipo_ajuste)
        VALUES (%s, %s, %s, %s)
    """, (producto_id, cantidad, motivo, tipo_ajuste))

    cursor.execute("SELECT cantidad_disponible FROM inventario WHERE producto_id = %s", (producto_id,))
    resultado = cursor.fetchone()
    if resultado:
        cantidad_actual = resultado[0]
        if tipo_ajuste.lower() == 'positivo':
            nueva_cantidad = cantidad_actual + cantidad
        elif tipo_ajuste.lower() == 'negativo':
            nueva_cantidad = cantidad_actual - cantidad
        else:
            nueva_cantidad = cantidad_actual
        cursor.execute("UPDATE inventario SET cantidad_disponible = %s WHERE producto_id = %s",
                       (nueva_cantidad, producto_id))
    else:
        nueva_cantidad = cantidad if tipo_ajuste.lower() == 'positivo' else 0
        cursor.execute("INSERT INTO inventario (producto_id, cantidad_disponible) VALUES (%s, %s)",
                       (producto_id, nueva_cantidad))

    conexion.commit()
    conexion.close()


def obtener_ajustes_inventario():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM ajuste_inventario")
    ajustes = cursor.fetchall()
    conexion.close()
    
    for ajuste in ajustes:
        producto = obtener_producto_por_id(ajuste["producto_id"])
        ajuste["producto"] = producto

    return ajustes

def obtener_ajuste_inventario_por_id(ajuste_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM ajuste_inventario WHERE id = %s", (ajuste_id,))
    ajuste = cursor.fetchone()
    conexion.close()

    if ajuste:
        producto = obtener_producto_por_id(ajuste["producto_id"])
        ajuste["producto"] = producto

    return ajuste


def actualizar_ajuste_inventario(ajuste_id, producto_id, cantidad, motivo, tipo_ajuste):
    conexion = obtener_conexion()
    cursor = conexion.cursor()

    cursor.execute("SELECT producto_id, cantidad, tipo_ajuste FROM ajuste_inventario WHERE id = %s", (ajuste_id,))
    ajuste_anterior = cursor.fetchone()

    if ajuste_anterior:
        prod_ant, cant_ant, tipo_ant = ajuste_anterior

        cursor.execute("SELECT cantidad_disponible FROM inventario WHERE producto_id = %s", (prod_ant,))
        inv_ant = cursor.fetchone()
        if inv_ant:
            cant_disp_ant = inv_ant[0]
            if tipo_ant.lower() == 'positivo':
                cant_disp_ant -= cant_ant
            elif tipo_ant.lower() == 'negativo':
                cant_disp_ant += cant_ant
            cursor.execute("UPDATE inventario SET cantidad_disponible = %s WHERE producto_id = %s",
                           (cant_disp_ant, prod_ant))

    cursor.execute("""
        UPDATE ajuste_inventario
        SET producto_id = %s, cantidad = %s, motivo = %s, tipo_ajuste = %s
        WHERE id = %s
    """, (producto_id, cantidad, motivo, tipo_ajuste, ajuste_id))

    cursor.execute("SELECT cantidad_disponible FROM inventario WHERE producto_id = %s", (producto_id,))
    inv_nuevo = cursor.fetchone()
    if inv_nuevo:
        cant_disp_nuevo = inv_nuevo[0]
        if tipo_ajuste.lower() == 'positivo':
            cant_disp_nuevo += cantidad
        elif tipo_ajuste.lower() == 'negativo':
            cant_disp_nuevo -= cantidad
        cursor.execute("UPDATE inventario SET cantidad_disponible = %s WHERE producto_id = %s",
                       (cant_disp_nuevo, producto_id))
    else:
        nueva_cantidad = cantidad if tipo_ajuste.lower() == 'positivo' else 0
        cursor.execute("INSERT INTO inventario (producto_id, cantidad_disponible) VALUES (%s, %s)",
                       (producto_id, nueva_cantidad))

    conexion.commit()
    conexion.close()





def eliminar_ajuste_inventario(ajuste_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM ajuste_inventario WHERE id = %s", (ajuste_id,))
    conexion.commit()
    conexion.close()
