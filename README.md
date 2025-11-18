# Papers Papelería - Backend API

**API RESTful para la gestión integral de una papelería, automatizando procesos de usuarios, productos, compras y ventas.**

## Descripción del Proyecto

Este proyecto es la API backend de "Papers Papelería", diseñada para servir como el cerebro de una aplicación de gestión. Proporciona un conjunto robusto de endpoints RESTful para manejar la lógica de negocio central, incluyendo la autenticación y autorización de usuarios con roles (Administradores, Empleados, Clientes, Proveedores), la administración de un catálogo de productos, el registro y seguimiento de compras a proveedores, y la gestión de ventas a clientes, todo ello persistido en una base de datos MySQL.

## Características Principales

* **Autenticación Segura:** Sistema de login basado en JSON Web Tokens (JWT) para sesiones sin estado.
* **Control de Acceso Basado en Roles (RBAC):** Gestión de permisos con roles definidos: `ADMINISTRADOR`, `EMPLEADO`, `CLIENTE`, `PROVEEDOR`.
* **Gestión de Usuarios:**
    * CRUD completo para `CLIENTES`, `PROVEEDORES` y `EMPLEADOS`.
    * Filtrado de usuarios por rol.
    * Control de estado `activo`/`inactivo`.
    * Protección de operaciones CRUD según el rol del usuario autenticado.
* **Gestión de Productos:**
    * CRUD completo de productos con detalles relevantes.
    * Catálogo de productos accesible sin autenticación (opcional, configurable).
    * Operaciones de modificación (creación, actualización, eliminación) restringidas a `ADMINISTRADOR` o `EMPLEADO`.
* **Gestión de Compras:**
    * Registro y seguimiento de todas las transacciones de compra a proveedores.
    * CRUD de registros de compras.
    * Acceso restringido a `ADMINISTRADOR` o `EMPLEADO`.
* **Gestión de Ventas:**
    * Registro de ventas realizadas a clientes.
    * CRUD de registros de ventas.
    * Acceso restringido a `ADMINISTRADOR` o `EMPLEADO`.
* **Seguridad Integral:** Implementación de Spring Security para cifrado de contraseñas (BCrypt), protección de endpoints y validación de tokens JWT.

## Tecnologías Utilizadas

### Backend

* **Lenguaje de Programación:** Java (JDK 17+)
* **Framework Principal:** Spring Boot 3.x
* **Base de Datos:** MySQL 8+
* **ORM/JPA:** Spring Data JPA (con Hibernate como proveedor)
* **Seguridad:** Spring Security 6.x, JSON Web Tokens (JWT)
* **Build Tool:** Apache Maven 3.x
* **Librerías Adicionales:** Project Lombok (para reducir el código boilerplate)

## Requisitos del Sistema

Para compilar y ejecutar este backend, necesitarás:

* **Java Development Kit (JDK):** Versión 17 o superior.
* **Apache Maven:** Versión 3.6.3 o superior.
* **Servidor MySQL:** Versión 8.0 o superior, accesible desde la máquina donde se ejecuta el backend.

## Instalación y Configuración

Sigue estos pasos para poner en marcha el backend de Papers Papelería.

### 1\. Configuración de la Base de Datos

1.  **Crea la Base de Datos:**
    Abre tu cliente MySQL (MySQL Workbench, DBeaver, línea de comandos) y ejecuta el siguiente comando para crear la base de datos:
    ```sql
    CREATE DATABASE IF NOT EXISTS papers_papeleria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
    *(Asegúrate de que tu usuario de MySQL tenga los permisos adecuados sobre esta base de datos).*
2.  **Tablas y Esquema:** El esquema de la base de datos (tablas) se generará automáticamente por Hibernate la primera vez que inicies la aplicación, según la configuración en `application.properties`.

### 2\. Configuración del Backend

1.  **Clona el Repositorio:**
    Navega al directorio donde quieras guardar el proyecto y clona el repositorio del backend:

    ```bash
    git clone <URL_DE_TU_REPOSITORIO_BACKEND>
    cd papers-papeleria-backend # Ajusta al nombre de tu carpeta
    ```

2.  **Actualiza `application.properties`:**
    Abre el archivo `src/main/resources/application.properties` y configura las siguientes propiedades:

    * **Conexión a MySQL:**
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/papers_papeleria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
        spring.datasource.username=tu_usuario_mysql
        spring.datasource.password=tu_password_mysql
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        ```
        *Reemplaza `tu_usuario_mysql` y `tu_password_mysql` con tus credenciales de MySQL.*
    * **JPA/Hibernate:**
        ```properties
        spring.jpa.hibernate.ddl-auto=update # 'update' para desarrollo, 'none' para producción. 'create-drop' si quieres que se borre y cree cada vez.
        spring.jpa.show-sql=true
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
        ```
    * **Puerto del Servidor:**
        ```properties
        server.port=8080 # O el puerto que desees
        ```
    * **Clave Secreta para JWT:**
        ```properties
        jwt.secret=UnaCadenaLargaYComplejaParaTuSecretoJWTQueNadieDebeAdivinar12345!@#$
        jwt.expiration=3600000 # Duración del token en milisegundos (1 hora = 3600000)
        ```
        **¡IMPORTANTE\!** En un entorno de producción, esta clave secreta debe ser mucho más robusta y gestionada de forma segura (ej. variables de entorno).

3.  **Compila el Proyecto:**
    Desde el directorio raíz del proyecto, ejecuta Maven para compilar:

    ```bash
    mvn clean install
    ```

4.  **Ejecuta la Aplicación:**
    Puedes ejecutar la aplicación Spring Boot directamente con Maven:

    ```bash
    mvn spring-boot:run
    ```

    O si has generado un JAR ejecutable, puedes correrlo así:

    ```bash
    java -jar target/papers-papeleria-backend-0.0.1-SNAPSHOT.jar # Ajusta el nombre del JAR si es diferente
    ```

La API estará disponible en `http://localhost:8080` (o el puerto que configuraste).

## Uso de la Aplicación (API Endpoints)

Una vez que el backend esté en funcionamiento, puedes interactuar con sus endpoints.

### 1\. Primer Inicio de Sesión y Obtención del Token JWT

Para acceder a la mayoría de los endpoints protegidos, necesitarás un token JWT.

* **Crear un Usuario Administrador Inicial (si no existe):**
    Si no tienes un usuario administrador, deberás insertarlo directamente en tu base de datos MySQL en la tabla `users` y `users_roles`, asegurándote de que tenga el rol `ADMINISTRADOR`.

* **Endpoint de Autenticación:**

    * **Método:** `POST`
    * **URL:** `http://localhost:8080/api/auth/login`
    * **Cuerpo de la Petición (JSON):**
        ```json
        {
            "username": "admin",
            "password": "admin123"
        }
        ```
    * **Respuesta:** Contendrá un JSON con el token JWT. Este token debe incluirse en el encabezado `Authorization` de las siguientes peticiones a endpoints protegidos (formato: `Authorization: Bearer <tu_token_jwt>`).

### 2\. Endpoints Principales

Aquí hay un resumen de los grupos de endpoints y sus requisitos:

| Módulo       | Endpoints Base           | Métodos y Roles Requeridos                                                                                                            |
| :----------- | :----------------------- | :------------------------------------------------------------------------------------------------------------------------------------ |
| **Auth** | `/api/auth/**`           | `POST /login` (permitAll)                                                                                                           |
| **Usuarios** | `/api/usuarios`          | `GET /` (filtrado por rol): `permitAll()` si se configura así, o `authenticated()` / `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` <br> `POST /`: `hasRole("ADMINISTRADOR")` (o `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` para ciertos roles) <br> `PUT /{id}`: `hasRole("ADMINISTRADOR")` <br> `DELETE /{id}`: `hasRole("ADMINISTRADOR")` |
| **Productos**| `/api/productos`         | `GET /`: `permitAll()` <br> `POST /`: `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` <br> `PUT /{id}`: `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` <br> `DELETE /{id}`: `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` |
| **Compras** | `/api/compras`           | `GET /`, `POST /`, `PUT /{id}`, `DELETE /{id}`: `hasAnyRole("ADMINISTRADOR", "EMPLEADO")`                                           |
| **Ventas** | `/api/ventas`            | `GET /`, `POST /`, `PUT /{id}`, `DELETE /{id}`: `hasAnyRole("ADMINISTRADOR", "EMPLEADO")`                                           |

*(Nota: Los roles como `hasAnyRole("ADMINISTRADOR", "EMPLEADO")` son configurables en `SecurityConfig.java`.)*