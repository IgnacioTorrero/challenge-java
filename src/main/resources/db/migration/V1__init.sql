CREATE TABLE punto_venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE
);


# CREATE TABLE costo_puntos (
#     idA INT NOT NULL,
#     idB INT NOT NULL,
#     costo DECIMAL(10,2) NOT NULL,
#     nombrePuntoB VARCHAR(255),
#     PRIMARY KEY (idA, idB),
#     CONSTRAINT fk_idA FOREIGN KEY (idA) REFERENCES punto_venta(id),
#     CONSTRAINT fk_idB FOREIGN KEY (idB) REFERENCES punto_venta(id)
# );

CREATE TABLE acreditacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    importe DECIMAL(10,2) NOT NULL,
    idPuntoVenta INT NOT NULL,
    fechaRecepcion DATE DEFAULT CURRENT_DATE,
    nombrePuntoVenta VARCHAR(255),
    CONSTRAINT fk_idPuntoVenta FOREIGN KEY (idPuntoVenta) REFERENCES punto_venta(id)
);
