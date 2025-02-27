# 🚀 Challenge Java - Proyecto Backend con Spring Boot

Este proyecto es una API REST desarrollada con **Spring Boot 3.4.1** y **Java 17**. Utiliza **MySQL 8** como base de datos y gestiona costos y acreditaciones de puntos de venta.

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes programas:

1. **Java 17 (JDK 17)**

    - Descargar desde: [Adoptium Temurin JDK 17](https://adoptium.net/temurin/releases/)
    - Verificar la instalación:
      ```sh
      java -version
      ```

2. **Maven 3.8+**

    - Descargar desde: [Apache Maven](https://maven.apache.org/download.cgi)
    - Verificar la instalación:
      ```sh
      mvn -version
      ```

3. **MySQL 8**

    - Descargar desde: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
    - Verificar la instalación:
      ```sh
      mysql --version
      ```

4. **Git**

    - Descargar desde: [Git](https://git-scm.com/downloads)
    - Verificar la instalación:
      ```sh
      git --version
      ```

5. **IntelliJ IDEA (Recomendado)**

    - Descargar desde: [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)

---

## 📦 Instalación y Configuración
### 1️⃣ Clonar el Repositorio
```sh
 git clone https://github.com/IgnacioTorrero/challenge-java.git
 cd challenge-java
```
### 2️⃣ Configurar la Base de Datos
Asegúrate de tener **MySQL** instalado y configurado.

#### Crear la base de datos manualmente:
```sql
CREATE DATABASE challenge_java;
```
La conexión a la base de datos se configura en el archivo `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/challenge_java?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=abc123
```
📌 **Asegúrate de cambiar las credenciales de acceso a MySQL si es necesario.**

### 3️⃣ Construir el Proyecto
```sh
mvn clean install
```
### 4️⃣ Ejecutar la Aplicación
```sh
mvn spring-boot:run
```
La API estará disponible en `http://localhost:8080`.

## 🛠️ Endpoints y Pruebas con Postman
A continuación, se detallan los endpoints y cómo probarlos en **Postman**.

### 1️⃣ Caché de Puntos de Venta
#### Obtener todos los puntos de venta
```http
GET /api/puntos-venta
```
📌 **Prueba en Postman:**
1. Abre Postman.
2. Crea una nueva solicitud `GET`.
3. Introduce la URL `http://localhost:8080/api/puntos-venta`.
4. Presiona **Send** y verifica la lista de puntos de venta.

#### Agregar un punto de venta
```http
POST /api/puntos-venta
```
**Body (JSON):**
```json
{
  "id": 11,
  "nombre": "San Juan"
}
```
📌 **Prueba en Postman:**
1. Crea una nueva solicitud `POST`.
2. Introduce la URL `http://localhost:8080/api/puntos-venta`.
3. En la pestaña **Body**, selecciona **raw** y el formato **JSON**.
4. Ingresa el JSON anterior.
5. Presiona **Send** y verifica que el punto de venta se haya agregado correctamente.

#### Actualizar un punto de venta
```http
PUT /api/puntos-venta
```
**Body (JSON):**
```json
{
  "id": 11,
  "nombre": "San Luis"
}
```

#### Eliminar un punto de venta
```http
DELETE /api/puntos-venta/{id}
```
Ejemplo para eliminar el punto de venta con id 11:
```http
DELETE /api/puntos-venta/11
```

---

### 2️⃣ Caché de Costos entre Puntos de Venta
#### Cargar un nuevo costo
```http
POST /api/costos
```
**Body (JSON):**
```json
{
  "idA": 1,
  "idB": 2
}
```
**Query Params:** `costo=5`

#### Remover un costo
```http
DELETE /api/costos
```
**Body (JSON):**
```json
{
  "idA": 1,
  "idB": 2
}
```

#### Consultar costos desde un punto de venta
```http
GET /api/costos/{idA}
```
Ejemplo:
```http
GET /api/costos/1
```

#### Obtener la ruta de menor costo entre dos puntos
```http
GET /api/costos/minimo
```
**Body (JSON):**
```json
{
  "idA": 1,
  "idB": 5
}
```

---

### 3️⃣ Acreditaciones
#### Crear una acreditación
```http
POST /api/acreditaciones
```
**Body (JSON):**
```json
{
  "importe": 100.50,
  "idPuntoVenta": 1
}
```

#### Obtener todas las acreditaciones
```http
GET /api/acreditaciones
```

## 🧪 Ejecución de Pruebas Unitarias
```sh
mvn test
```
Verifica la cobertura de pruebas en la terminal.

## 📌 Notas Importantes
- **La base de datos debe estar creada antes de correr la aplicación.**
- **Los puntos de venta y costos iniciales están en caché y pueden modificarse en runtime.**
- **Las pruebas de integración pueden realizarse en Postman con los ejemplos proporcionados.**

---

Este README proporciona instrucciones completas para levantar la API y probarla correctamente en otro entorno. 🚀





