-- ===== CREAMOS LA BASE DE DATOS =====
CREATE DATABASE IF NOT EXISTS pedidos_db;
USE pedidos_db;

-- ===== HACEMOS UNA LIMPIEZA PREVIA =====
-- El orden de los DROP es clave para que no exploten las Foreign Keys
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuario;

-- ===== CREAMOS LAS TABLAS =====

-- Tabla Categoría
CREATE TABLE categoria (
                           id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                           eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                           createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           nombre VARCHAR(50) NOT NULL UNIQUE,
                           descripcion VARCHAR(150) NOT NULL
);

-- Tabla Producto
CREATE TABLE producto (
                          id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                          eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                          createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          nombre VARCHAR(50) NOT NULL,
                          precio DECIMAL(10,2) NOT NULL,
                          descripcion VARCHAR(150) NOT NULL,
                          stock INT NOT NULL DEFAULT 1,
                          image VARCHAR(500) NOT NULL,
                          disponible BOOLEAN NOT NULL DEFAULT TRUE,

                          id_categoria BIGINT NOT NULL,
                          FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

-- Tabla Usuario
CREATE TABLE usuario (
                         id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                         eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                         createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         mail VARCHAR(150) UNIQUE NOT NULL,
                         celular VARCHAR(15) NOT NULL,
                         contrasenia VARCHAR(50) NOT NULL,
                         rol VARCHAR(20) NOT NULL
);

-- Tabla Pedido
CREATE TABLE pedido (
                        id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                        createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        fecha DATETIME NOT NULL, -- CORRECCIÓN: DATETIME para que coincida con LocalDateTime
                        estado VARCHAR(50) NOT NULL,
                        total DECIMAL(10,2),
                        forma_pago VARCHAR(20),

                        id_usuario BIGINT NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Tabla Detalle Pedido
CREATE TABLE detalle_pedido (
                                id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                                eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                                createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                cantidad INT NOT NULL DEFAULT 1,
                                precio_unitario DECIMAL(10,2) NOT NULL, -- CORRECCIÓN: Agregado el precio unitario
                                subtotal DECIMAL(10,2) NOT NULL,

                                id_pedido BIGINT NOT NULL,
                                id_producto BIGINT NOT NULL,

                                FOREIGN KEY (id_pedido) REFERENCES pedido(id) ON DELETE CASCADE,
                                FOREIGN KEY (id_producto) REFERENCES producto(id)
);


-- ===== INSERCIÓN DE DATOS DE PRUEBA =====

-- Insertar Categorías
INSERT INTO categoria (nombre, descripcion) VALUES
                                                ('Hamburguesas', 'Variedad de hamburguesas caseras con papas'),
                                                ('Pizzas', 'Pizzas al horno de barro'),
                                                ('Bebidas', 'Gaseosas, aguas y jugos naturales');

-- Insertar Productos
INSERT INTO producto (nombre, precio, descripcion, stock, image, disponible, id_categoria) VALUES
                                                                                               ('Hamburguesa Clásica', 4500.00, 'Medallón de carne, queso, lechuga y tomate', 20, 'url_imagen_1', TRUE, 1),
                                                                                               ('Hamburguesa Completa', 5500.00, 'Doble carne, cheddar, bacon y huevo frito', 15, 'url_imagen_2', TRUE, 1),
                                                                                               ('Pizza Muzzarella', 6000.00, 'Salsa de tomate, muzzarella y aceitunas', 10, 'url_imagen_3', TRUE, 2),
                                                                                               ('Pizza Fugazzeta', 6800.00, 'Abundante cebolla, muzzarella y orégano', 8, 'url_imagen_4', TRUE, 2),
                                                                                               ('Coca Cola 500ml', 1500.00, 'Gaseosa común', 50, 'url_imagen_5', TRUE, 3),
                                                                                               ('Agua Mineral 500ml', 1200.00, 'Agua sin gas', 40, 'url_imagen_6', TRUE, 3);

-- Insertar Usuarios
INSERT INTO usuario (nombre, apellido, mail, celular, contrasenia, rol) VALUES
                                                                            ('Facundo', 'Ramirez', 'facundo@mail.com', '2615551234', 'password123', 'ADMIN'),
                                                                            ('Juan', 'Perez', 'juan.perez@mail.com', '2615555678', 'cliente2026', 'USUARIO'),
                                                                            ('Maria', 'Gomez', 'maria.gomez@mail.com', '2615559012', 'operador456', 'USUARIO');

-- Insertar un Pedido de prueba
INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario) VALUES
    (CURRENT_TIMESTAMP(), 'PENDIENTE', 10500.00, 'EFECTIVO', 2);

-- Insertar Detalles del pedido anterior (Actualizado con precios unitarios)
INSERT INTO detalle_pedido (cantidad, precio_unitario, subtotal, id_pedido, id_producto) VALUES
                                                                                             (1, 4500.00, 4500.00, 1, 1),
                                                                                             (1, 6000.00, 6000.00, 1, 3);