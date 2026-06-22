# Food Store – Sistema de Gestión de Pedidos

Trabajo integrador de la materia Programación 2, correspondiente al 2do semestre de la Tecnicatura en Programación de la UTP.

Este sistema permite gestionar productos, categorías, usuarios y pedidos de un comercio de alimentos. Está desarrollado en Java 21, utiliza JDBC como capa de acceso a datos y MariaDB como motor de base de datos. La interfaz es por consola, con un menú interactivo que integra todas las operaciones del sistema.

## Requisitos

- Java 21 o superior
- MariaDB o MySQL
- Maven
- IntelliJ IDEA (entorno de desarrollo recomendado)

## Configuración

### Base de datos

Para inicializar la base de datos, ejecutar el script SQL incluido en el proyecto:

```sql
source src/integrador/prog2/config/schema.sql
```

Este script crea la base de datos `pedidos_db` con todas las tablas, claves foráneas y datos de prueba (3 categorías, 6 productos, 3 usuarios y 1 pedido).

### Conexión

Verificar que los datos de conexión en `DatabaseConnectionPool.java` coincidan con los de su entorno local:

```java
config.setJdbcUrl("jdbc:mariadb://localhost:3306/pedidos_db");
config.setUsername("root");
config.setPassword("");
```

Por defecto se utiliza el puerto `3306`, usuario `root` y contraseña vacía. Modificar según corresponda.

## Ejecución

Para ejecutar el proyecto desde IntelliJ IDEA:

1. Abrir el proyecto en IntelliJ IDEA.
2. Navegar al archivo `src/integrador/prog2/main.java`.
3. Hacer clic derecho sobre el archivo y seleccionar **"Run 'main.main()'"**.
4. Alternativamente, ubicar el ícono verde junto a la función `main()` y hacer clic en él.

IntelliJ compilará y ejecutará el proyecto automáticamente.

Otra opción es ejecutar desde la terminal con Maven:

```bash
mvn compile exec:java -Dexec.mainClass="integrador.prog2.main"
```

## Estructura del proyecto

```
src/integrador/prog2/
├── main.java              # Punto de entrada y menú principal
├── config/
│   ├── DatabaseConnectionPool.java  # Pool de conexiones (HikariCP)
│   └── schema.sql                  # Script de creación de tablas y datos
├── entities/              # Entidades del dominio
│   ├── Base.java          # Clase abstracta (id, eliminado, createdAt)
│   ├── Calculable.java    # Interfaz para cálculo de totales
│   ├── Producto.java
│   ├── Categoria.java
│   ├── Usuario.java
│   ├── Pedido.java
│   └── DetallePedido.java
├── enums/                 #Enumeraciones del sistema
│   ├── Estado.java        # PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO
│   ├── FormaPago.java     # TARJETA, TRANSFERENCIA, EFECTIVO
│   └── Rol.java           # ADMIN, USUARIO
├── DAO/                   # Capa de acceso a datos (JDBC)
│   ├── GenericDAO.java    # Interfaz genérica con operaciones CRUD
│   ├── ProductoDAO.java
│   ├── CategoriaDAO.java
│   ├── UsuarioDAO.java
│   ├── PedidoDAO.java
│   └── DetallePedidoDAO.java
├── services/              # Lógica de negocio y validaciones
│   ├── ProductoService.java
│   ├── CategoriaService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
├── controllers/           # Coordinación entre vista y servicio
│   ├── ProductoController.java
│   ├── CategoriaController.java
│   ├── UsuarioController.java
│   └── PedidoController.java
└── exception/             # Excepciones propias del dominio
    ├── ValidacionNegocioException.java
    ├── EntidadNoEncontradaException.java
    ├── StockInsuficienteException.java
    └── PersistenciaException.java
```

## Modelo de base de datos

Las relaciones entre entidades se representan de la siguiente manera:

```
categoria (1) ─────< (N) producto
pedido    (1) ─────< (N) detalle_pedido (N) >───── (1) producto
pedido    (N) >───── (1) usuario
```

- **categoria → producto**: cada producto pertenece a una categoría (FK `id_categoria`).
- **pedido → usuario**: cada pedido corresponde a un usuario (FK `id_usuario`).
- **detalle_pedido → producto**: cada línea de detalle referencia un producto (FK `id_producto`).
- **detalle_pedido → pedido**: cada línea de detalle pertenece a un pedido (FK `id_pedido`).

Todas las eliminaciones son lógicas mediante el campo `eliminado`, lo que permite conservar el historial de datos.

## Funcionalidades

### Categorías
- Listar todas las categorías registradas.
- Crear nuevas categorías con nombre y descripción.
- Editar nombre y descripción de una categoría existente.
- Eliminar una categoría (solo si no posee productos asociados).

### Productos
- Listar todos los productos con su categoría asociada.
- Filtrar productos por categoría.
- Crear productos con nombre, descripción, precio, stock, imagen y categoría.
- Editar los atributos de un producto existente.
- Eliminar un producto (baja lógica).

### Usuarios
- Listar todos los usuarios del sistema.
- Crear usuarios con nombre, apellido, correo electrónico, celular, contraseña y rol.
- Editar datos personales de un usuario.
- Eliminar un usuario (baja lógica).

### Pedidos
- Listar todos los pedidos con usuario, estado, forma de pago y total.
- Filtrar pedidos por usuario.
- Crear un pedido: selección de usuario, incorporación de productos con cantidad y forma de pago.
- Actualizar el estado del pedido (PENDIENTE → CONFIRMADO → TERMINADO / CANCELADO).
- Actualizar la forma de pago de un pedido.
- Eliminar un pedido (baja lógica).

### Transacciones

La creación de un pedido junto con sus detalles se realiza dentro de una **transacción**. En caso de error en cualquier paso, se ejecuta un **rollback** que garantiza la integridad de los datos.

## Manejo de excepciones

El sistema define excepciones personalizadas para diferenciar errores de negocio de errores de persistencia:

| Excepción | Descripción |
|---|---|
| `ValidacionNegocioException` | Violación de reglas de negocio (ej: stock negativo, categoría nula) |
| `EntidadNoEncontradaException` | Intento de operar con una entidad inexistente |
| `StockInsuficienteException` | Stock insuficiente para satisfacer la solicitud |
| `PersistenciaException` | Errores relacionados con la base de datos |

Las excepciones se capturan en la capa de controllers y se presentan en consola con un formato legible.

## Arquitectura

El proyecto sigue una arquitectura en capas:

```
Vista (main.java) → Controller → Service → DAO → Base de datos
```

- **Controller**: recibe las peticiones del usuario, delega al service y maneja la presentación de errores.
- **Service**: contiene la lógica de negocio y validaciones antes de persistir.
- **DAO**: accede a la base de datos mediante JDBC, ejecutando consultas parametrizadas.
- **Config**: administra el pool de conexiones con HikariCP para optimizar el rendimiento.

## INTEGRANTES - GRUPO 6

- Matias Vargas
- Facundo Deseff
- Facundo Ramirez
- Santino Godoy

Trabajo integrador – Programación 2, Tecnicatura en Programación, UTP 2do Semestre.
