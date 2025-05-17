import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
import requests
import xml.etree.ElementTree as ET
from decimal import Decimal, ROUND_HALF_UP

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


URL_SERVICIO_INVENTARIO = None

def obtener_url_servicio_inventario():
    global URL_SERVICIO_INVENTARIO
    try:
        response = requests.get("http://localhost:8090/eureka/apps/SERVICIO-INVENTARIO")
        if response.status_code == 200:
            root = ET.fromstring(response.content)
            instance = root.find('instance')
            if instance is not None:
                host = instance.find('hostName').text
                port = instance.find('port').text
                URL_SERVICIO_INVENTARIO = f"http://{host}:{port}"
                print(f"URL servicio-inventario actualizada: {URL_SERVICIO_INVENTARIO}")
                return URL_SERVICIO_INVENTARIO
            else:
                print("No se encontró la instancia en la respuesta de Eureka.")
                URL_SERVICIO_INVENTARIO = None
                return None
        else:
            print(f"Error al consultar Eureka: status {response.status_code}")
            URL_SERVICIO_INVENTARIO = None
            return None
    except Exception as e:
        print(f"Error al obtener URL del servicio inventario: {e}")
        URL_SERVICIO_INVENTARIO = None
        return None
obtener_url_servicio_inventario()

def obtener_inventario_por_producto(producto_id):
    global URL_SERVICIO_INVENTARIO
    if not URL_SERVICIO_INVENTARIO:
        if not obtener_url_servicio_inventario():
            return {"error": "No se pudo resolver la URL del servicio-inventario"}
    try:
        response = requests.get(f"{URL_SERVICIO_INVENTARIO}/inventario/{producto_id}")
        if response.status_code == 200:
            return response.json()
        else:
            return {"error": "Inventario no encontrado"}
    except Exception as e:
        print(f"Error al contactar con servicio-inventario: {str(e)}")
        obtener_url_servicio_inventario()
        return {"error": "Error al contactar con servicio-inventario"}





def crear_detalle_compra(compra_id, producto_id, cantidad):
    # Obtener producto y su precio
    producto = obtener_producto_por_id(producto_id)
    if 'precio' not in producto:
        raise Exception("No se pudo obtener el precio del producto")

    precio_unitario = Decimal(str(producto['precio']))
    total = (precio_unitario * cantidad).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)

    # Insertar detalle de compra
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        INSERT INTO detalle_compra_proveedor (compra_id, producto_id, cantidad, precio_unitario, total)
        VALUES (%s, %s, %s, %s, %s)
    """, (compra_id, producto_id, cantidad, float(precio_unitario), float(total)))
    conexion.commit()

    # Obtener inventario actual (por producto_id)
    try:
        print(f"Consultando inventario en: {URL_SERVICIO_INVENTARIO}/inventario/{producto_id}")
        response = requests.get(f"{URL_SERVICIO_INVENTARIO}/inventario/{producto_id}")
        response.raise_for_status()
        inventario = response.json()

        inventario_id = inventario.get("id")  # Usar el ID correcto de la tabla inventario
        cantidad_actual = inventario.get("cantidad_disponible", 0) or 0
        nueva_cantidad = cantidad_actual + cantidad

        update_data = {
            "cantidad_disponible": nueva_cantidad,
            "fecha_vencimiento": inventario.get("fecha_vencimiento")  # Mantenemos la fecha actual
        }

        print(f"Actualizando inventario ID {inventario_id} con: {update_data}")
        put_response = requests.put(f"{URL_SERVICIO_INVENTARIO}/inventario/{inventario_id}", json=update_data)
        put_response.raise_for_status()
        print("Inventario actualizado correctamente")

    except Exception as e:
        print(f"Error al actualizar inventario: {e}")

    # Registrar movimiento de inventario
    movimiento_data = {
        "producto_id": producto_id,
        "tipo_movimiento": "entrada",
        "cantidad": cantidad
    }
    try:
        response = requests.post(f"{URL_SERVICIO_INVENTARIO}/movimientos_inventario", json=movimiento_data)
        response.raise_for_status()
        print("Movimiento de inventario creado correctamente")
    except Exception as e:
        print(f"Error creando movimiento inventario: {e}")

    # Actualizar total de la compra con IGV
    total_con_igv = (total * Decimal('1.18')).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
    cursor.execute("UPDATE compra_proveedor SET total = %s WHERE id = %s", (float(total_con_igv), compra_id))
    conexion.commit()
    conexion.close()
    



def obtener_detalles_compra():
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM detalle_compra_proveedor")
    detalles = cursor.fetchall()

    for detalle in detalles:
        cursor_compra = conexion.cursor(dictionary=True)
        cursor_compra.execute("SELECT * FROM compra_proveedor WHERE id = %s", (detalle["compra_id"],))
        compra = cursor_compra.fetchone()
        cursor_compra.close()

        if compra:
            cursor_proveedor = conexion.cursor(dictionary=True)
            cursor_proveedor.execute("SELECT * FROM proveedor WHERE id = %s", (compra["proveedor_id"],))
            proveedor = cursor_proveedor.fetchone()
            cursor_proveedor.close()
            compra["proveedor"] = proveedor

        detalle["compra"] = compra

        producto = obtener_producto_por_id(detalle["producto_id"])
        detalle["producto"] = producto

    conexion.close()
    return detalles





def obtener_detalle_compra_por_id(detalle_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor(dictionary=True)
    cursor.execute("SELECT * FROM detalle_compra_proveedor WHERE id = %s", (detalle_id,))
    detalle = cursor.fetchone()
    conexion.close()
    return detalle

def actualizar_detalle_compra(detalle_id, compra_id, producto_id, cantidad, precio_unitario, total):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("""
        UPDATE detalle_compra_proveedor
        SET compra_id = %s, producto_id = %s, cantidad = %s, precio_unitario = %s, total = %s
        WHERE id = %s
    """, (compra_id, producto_id, cantidad, precio_unitario, total, detalle_id))
    conexion.commit()
    conexion.close()

def eliminar_detalle_compra(detalle_id):
    conexion = obtener_conexion()
    cursor = conexion.cursor()
    cursor.execute("DELETE FROM detalle_compra_proveedor WHERE id = %s", (detalle_id,))
    conexion.commit()
    conexion.close()
