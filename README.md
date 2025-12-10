# Sistema de Gestión - Mueblería Los Hermanos S.A.

Este proyecto es una solución integral Full-Stack para la administración de inventario, gestión de variantes y generación de cotizaciones y ventas para la "Mueblería Los Hermanos". El sistema permite controlar el catálogo de productos, personalizar muebles con variantes (que afectan el precio) y realizar ventas que descuentan stock en tiempo real.

## Tecnologías Utilizadas

### Backend (API REST)
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3
* **Base de Datos:** MySQL 8.0
* **ORM:** Spring Data JPA (Hibernate)
* **Build Tool:** Maven
* **Containerización:** Docker & Docker Compose

### Frontend (Cliente Web)
* **Librería:** React.js
* **Estilos:** Bootstrap 5 (React-Bootstrap)
* **Iconos:** React Bootstrap Icons
* **Comunicación:** Fetch API

---

## Características Principales

1.  **Gestión de Catálogo (CRUD):**
    * Crear, listar, editar y eliminar (desactivar) muebles.
    * Control de atributos: Precio base, Stock, Tipo, Material y Tamaño.
2.  **Gestión de Variantes:**
    * Registro de personalizaciones (ej: "Barniz Premium", "Ruedas").
    * Las variantes añaden un costo adicional al precio base del mueble.
3.  **Cotizador y Punto de Venta:**
    * Creación de cotizaciones con múltiples ítems.
    * Cálculo automático de precios (Precio Base + Variantes * Cantidad).
    * **Confirmación de Venta:** Convierte una cotización en venta y **descuenta el stock** automáticamente.
    * Validación de stock insuficiente.

---

## Instalación y Ejecución

### Prerrequisitos
* Docker y Docker Compose instalados.
* Node.js y npm (para el frontend local).

### Levantar el proyecto (Docker)
El proyecto incluye un archivo `docker-compose.yml` que orquesta la base de datos, la aplicación Java y la aplicación web de React.

```bash
# En la raíz del proyecto (donde está docker-compose.yml)
docker-compose up --build
```
**Nota:** Esto levantará el backend en el puerto 8090, MySQL en el puerto 3306 y el frontend en el puerto 3000. La primera vez puede tardar unos minutos en compilar y descargar dependencias.

---

# Arquitectura y Patrones de Diseño
El proyecto implementa una arquitectura en capas (Controller -> Service -> Repository) y utiliza varios patrones de diseño para asegurar un código limpio y mantenible:

1. **Patrón Builder:**

* Implementado a través de Lombok (@Builder). Se utiliza extensivamente para la creación de objetos complejos como Mueble, Variante y ItemCotizacion de forma fluida y legible en el DataSeeder y los Servicios.

2. **Patrón DTO (Data Transfer Object):**

* Se utilizan objetos como CotizacionRequestDTO e ItemCotizacionRequestDTO para transferir datos desde el frontend hacia el backend, desacoplando la capa de presentación de la capa de persistencia.

3. **Patrón Repository:**

* Uso de interfaces que extienden de JpaRepository (MuebleRepository, CotizacionRepository) para abstraer la lógica de acceso a datos.

4. **Patrón Singleton:**

* Gestionado por el contenedor de Spring para los @Service y @RestController, asegurando una única instancia de los componentes de lógica de negocio.

---
# Endpoints Principales de la API
La API corre en http://localhost:8090/api. Algunos endpoints clave:

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/muebles` | Listar todos los muebles activos. |
| `POST` | `/api/muebles` | Crear un nuevo mueble. |
| `PUT` | `/api/muebles/{id}` | Actualizar un mueble existente. |
| `DELETE`| `/api/muebles/{id}` | Desactivar un mueble (borrado lógico). |
| `POST` | `/api/variantes` | Crear una nueva variante de precio. |
| `POST` | `/api/cotizaciones` | Generar una nueva cotización (PENDIENTE). |
| `POST` | `/api/cotizaciones/{id}/confirmar` | Confirmar venta y descontar stock. |

---
# Contexto de Evaluación
Este proyecto fue desarrollado para la Evaluación 2 de Ingeniería de Software. Cumple con los requisitos de:

* Conexión Spring Boot + MySQL.

* Implementación de API REST.

* Uso de patrones de diseño.

* Testing unitario e integración (JUnit & Mockito).

* Manejo de reglas de negocio complejas (Cálculo de precios y Stock).

**Autor:** Cristóbal Morales.
