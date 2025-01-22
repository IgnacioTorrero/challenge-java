# Challenge Java Project

En la primera parte están algunas observaciones sobre el entorno del proyecto, patrones de diseño, etc. En la segunda,
abajo de todo, está la guía para testear los endpoints paso a paso con Postman.

---

## Requisitos Previos

- **IDE Recomendado**: IntelliJ IDEA (con soporte para Maven y Spring Boot) con licencia.
    - Ahí tengo todos los plugins necesarios para el trabajo.
- **Java Development Kit (JDK)**: Versión 17.
- **Spring Boot**: Creado con Spring Initializr.
- **Base de Datos**: MySQL 8.0.36.

---

## Configuración del Proyecto

1. **Clonación del Repositorio**:
    - Recomiendo clonar el repo desde Git Desktop y abrir el mismo IDE desde ahí.

2. **Dependencias Maven**:
    - Usé el **Maven Wrapper** por default.
    - Para indexar las dependencias:
      ```bash
      mvn clean install
      ```
    - En IntelliJ IDEA, podes usar las opciones para sincronizar Maven desde la barrita lateral (M).

3. **Base de Datos (MySQL)**:
    - Usa MySQL y crea una base de datos `challenge_java`.
    - Detalles de la conexión están en el archivo `application.properties`.
    - Comandos para inicializar la BBDD:
      ```bash
      mysql -u root -p
      CREATE DATABASE challenge_java;
      USE challenge_java;
      ```
    - Las tablas se generan automáticamente con Hibernate corriendo el proyecto.

4. **Compilación e Inicialización**:
    - Para compilar y ejecutar el proyecto:
      ```bash
      mvn clean
      mvn compile
      mvn spring-boot:run
      ```

---

## Estructura del Proyecto

El proyecto sigue el patrón de diseño **Modelo-Vista-Controlador (MVC)**:
- **Modelo**:
    - Clases entidad y repositorios que interactúan con la base de datos.
- **Vista**:
    - Usar **Postman** para probar los endpoints.
- **Controlador**:
    - Clases REST Controllers que envían las solicitudes HTTP.

---

## Cobertura de Pruebas

- Abarqué el **100% de cobertura en pruebas unitarias**.

---

## Notas Finales

- No usé docker porque más allá de un curso, nunca lo trabajé a nivel profesional.
- Si MySQL o las dependencias te complican, usa estos comandos:
```bash
mvn clean
mvn compile
mvn spring-boot:run
```

---

# Pruebas Postman

---

## Punto 1: Caché Puntos de Venta

### 1. Recuperar todos los puntos de venta presentes en el caché
- **Método**: `GET`
- **URL**: `http://localhost:8080/api/puntos-venta`
- **Parámetros**: Ninguno
- **Resultado**:
  ```json
  [
    { "id": 1, "nombre": "CABA" },
    { "id": 2, "nombre": "GBA_1" },
    ...
  ]

---

## 2. Ingresar un nuevo punto de venta

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/puntos-venta`
- **Parámetros**:
    - `id`: Identificador único (por ejemplo, `11`).
    - `nombre`: Nombre del nuevo punto (por ejemplo, `La Rioja`).
- **URL Completa**: `http://localhost:8080/api/puntos-venta?id=11&nombre=LaRioja`
- **Resultado**: `200 OK`

---

## 3. Actualizar un punto de venta

- **Método**: `PUT`
- **URL**: `http://localhost:8080/api/puntos-venta/{id}` (reemplazar `{id}` con el ID del punto de venta).
- **Parámetros**:
    - `nombre`: Nuevo nombre del punto (por ejemplo, `CABA Actualizado`).
- **URL Completa**: `http://localhost:8080/api/puntos-venta/1?nombre=CapitalFederal`
- **Resultado**: `200 OK`

---

## 4. Borrar un punto de venta

- **Método**: `DELETE`
- **URL**: `http://localhost:8080/api/puntos-venta/{id}` (reemplazar `{id}` con el ID del punto de venta).
- **URL Completa**: `http://localhost:8080/api/puntos-venta/10`
- **Resultado**: `200 OK`

---

## Punto 2: Caché Puntos de Costos

## 1. Cargar un nuevo costo entre un punto de venta A y un punto de venta B

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/costos`
- **Parámetros**:
    - `idA`: El ID del punto de venta de origen (por ejemplo, `1`).
    - `idB`: El ID del punto de venta de destino (por ejemplo, `2`).
    - `costo`: El costo entre los puntos (por ejemplo, `5.0`).
- **URL Completa**: `http://localhost:8080/api/costos?idA=1&idB=2&costo=5.0`
- **Resultado**: `200 OK`

---

## 2. Remover un costo entre un punto de venta A y un punto de venta B

- **Método**: `DELETE`
- **URL**: `http://localhost:8080/api/costos`
- **Parámetros**:
    - `idA`: El ID del punto de venta de origen (por ejemplo, `1`).
    - `idB`: El ID del punto de venta de destino (por ejemplo, `2`).
- **URL Completa**: `http://localhost:8080/api/costos?idA=1&idB=2`
- **Resultado**: `200 OK`

---

## 3. Consultar los puntos de venta directamente desde un punto de venta A y los costos asociados

- **Método**: `GET`
- **URL**: `http://localhost:8080/api/costos/{idA}` (reemplazar `{idA}` con el ID del punto de venta de origen, por ejemplo, `1`).
- **URL Completa**: `http://localhost:8080/api/costos/1`
- **Resultado**: Lista de costos directos desde el punto especificado, en formato JSON. Ejemplo:
  ```json
  [
      { "idA": 1, "idB": 2, "costo": 2.0, "nombrePuntoB": "GBA_1" },
      { "idA": 1, "idB": 3, "costo": 3.0, "nombrePuntoB": "GBA_2" },
      { "idA": 1, "idB": 4, "costo": 11.0, "nombrePuntoB": "Santa Fe" }
  ]

---

## 4. N/A

Este punto no lo hice por la dificultad para entender la resolución. Más tarde identifiqué que la solución se basaba en el uso del algoritmo de Dijkstra, el cual no usé nunca en mi vida.
La única forma de incorporarlo habría sido usando Internet, pero como esa no es la idea lo omití.

---

## Punto 3: Acreditación

## 1. Agregar una acreditación

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/acreditaciones`
- **Parámetros**:
    - `importe`: `100.0`
    - `idPuntoVenta`: `1`

### Postman
- **Método**: `POST`
- **URL Completa**: `http://localhost:8080/api/acreditaciones?importe=100.0&idPuntoVenta=1`

### Respuesta (200 OK)
```json
{
    "id": 1,
    "importe": 100.0,
    "idPuntoVenta": 1,
    "nombrePuntoVenta": "CABA",
    "fechaRecepcion": "2025-01-21"
}
```

---

## 2. Obtener acreditaciones

- **Método**: `GET`
- **URL**: `http://localhost:8080/api/acreditaciones`

### Respuesta (200 OK)
```json
[
    {
        "id": 1,
        "importe": 100.0,
        "idPuntoVenta": 1,
        "nombrePuntoVenta": "CABA",
        "fechaRecepcion": "2025-01-21"
    }
]
```

## Dato a tener en cuenta

Puse validaciones sobre:

- IDs inválidos o no registrados.
- Montos menores a cero.

También pueden testearlas, ya que deberían funcionar.
