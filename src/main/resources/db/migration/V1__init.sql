CREATE TABLE IF NOT EXISTS point_sale (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS cost_points (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        idA BIGINT,
        idB BIGINT,
        cost DOUBLE
);

CREATE TABLE IF NOT EXISTS accreditation (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        amount DOUBLE,
        id_point_sale BIGINT,
        point_sale_name VARCHAR(255),
        receipt_date DATE
    );

CREATE TABLE IF NOT EXISTS username (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role ENUM('ADMIN', 'USER')
    );

-- Datos de ejemplo
INSERT INTO point_sale (name) VALUES
    ('CABA'), ('GBA_1'), ('GBA_2'), ('Santa Fe'), ('Córdoba'),
    ('Misiones'), ('Salta'), ('Chubut'), ('Santa Cruz'), ('Catamarca');
