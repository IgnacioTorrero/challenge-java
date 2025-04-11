CREATE TABLE IF NOT EXISTS punto_venta (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS costo_puntos (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        idA BIGINT,
        idB BIGINT,
        costo DOUBLE
);

CREATE TABLE IF NOT EXISTS acreditacion (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        importe DOUBLE,
        id_punto_venta BIGINT,
        nombre_punto_venta VARCHAR(255),
        fecha_recepcion DATE
    );

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    rol ENUM('ADMIN', 'USER')
    );

-- Datos de ejemplo
INSERT INTO punto_venta (nombre) VALUES
    ('CABA'), ('GBA_1'), ('GBA_2'), ('Santa Fe'), ('Córdoba'),
    ('Misiones'), ('Salta'), ('Chubut'), ('Santa Cruz'), ('Catamarca');
