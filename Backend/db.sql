-- Base de datos cliente_db
CREATE DATABASE cliente_db;

USE cliente_db;

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni VARCHAR(15) UNIQUE,
    ruc VARCHAR(15) UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255),
    descuento DECIMAL(5, 2),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE beneficio_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    descripcion VARCHAR(255),
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE pago_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    monto DECIMAL(10, 2),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metodo_pago VARCHAR(50),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE compra_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    total DECIMAL(10, 2),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Base de datos pedido_db
CREATE DATABASE pedido_db;

USE pedido_db;

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2),
    estado VARCHAR(50),
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id)
);

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT,
    producto_id INT,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    total DECIMAL(10, 2),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);

-- Base de datos venta_db
CREATE DATABASE venta_db;

USE venta_db;

CREATE TABLE venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2),
    estado VARCHAR(50),
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id)
);

CREATE TABLE detalle_venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT,
    producto_id INT,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    total DECIMAL(10, 2),
    FOREIGN KEY (venta_id) REFERENCES venta(id)
);

-- Base de datos producto_db
CREATE DATABASE producto_db;

USE producto_db;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100)
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    categoria_id INT,
    descripcion TEXT,
    precio DECIMAL(10, 2),
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Base de datos proveedor_db
CREATE DATABASE proveedor_db;

USE proveedor_db;

CREATE TABLE proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    email VARCHAR(100)
);

CREATE TABLE producto_proveedor (
    proveedor_id INT,
    producto_id INT,
    PRIMARY KEY (proveedor_id, producto_id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id),
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);

CREATE TABLE compra_proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id INT,
    total DECIMAL(10, 2),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);

CREATE TABLE detalle_compra_proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT,
    producto_id INT,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    total DECIMAL(10, 2),
    FOREIGN KEY (compra_id) REFERENCES compra_proveedor(id),
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);

-- Base de datos compra_db
CREATE DATABASE compra_db;

USE compra_db;

CREATE TABLE compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id INT,
    total DECIMAL(10, 2),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor_db.proveedor(id)
);

CREATE TABLE detalle_compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT,
    producto_id INT,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    total DECIMAL(10, 2),
    FOREIGN KEY (compra_id) REFERENCES compra(id),
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);

-- Base de datos inventario_db
CREATE DATABASE inventario_db;

USE inventario_db;

CREATE TABLE movimiento_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT,
    tipo_movimiento VARCHAR(50),
    cantidad INT,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);

CREATE TABLE ajuste_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT,
    cantidad INT,
    motivo VARCHAR(255),
    tipo_ajuste VARCHAR(50),
    fecha_ajuste TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT UNIQUE,
    cantidad_disponible INT,
    stock_minimo INT DEFAULT 0,
    fecha_vencimiento DATE,
    FOREIGN KEY (producto_id) REFERENCES producto_db.producto(id)
);
