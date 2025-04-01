CREATE TABLE acreditacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    importe DECIMAL(10,2) NOT NULL,
    idPuntoVenta INT NOT NULL,
    fechaRecepcion DATE DEFAULT CURRENT_DATE,
    nombrePuntoVenta VARCHAR(255),
    CONSTRAINT fk_idPuntoVenta FOREIGN KEY (idPuntoVenta) REFERENCES punto_venta(id)
);

CREATE TABLE costos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_a INT NOT NULL,
    id_b INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL
);
