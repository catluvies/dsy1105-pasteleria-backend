# Pastelería Mil Sabores - Aplicación Móvil E-Commerce Backend

* **Asignatura:** Desarrollo de Aplicaciones Móviles
* **Sección:** DSY1105-002D
* **Profesor:** Ronald Villalobos
* **Integrantes:** Federico Pereira - Sebastián Robles - Carlos Miranda - Anyara Rosso

Backend desarrollado en Spring Boot para la aplicación móvil de gestión de productos de pastelería.

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** - para persistencia de datos
- **Oracle Autonomous Database** - base de datos en la nube
- **Maven** - gestión de dependencias
- **Railway** - plataforma de despliegue

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Java Development Kit (JDK) 17 o superior
- Maven 3.8+
- Oracle Wallet (para conexión a la base de datos)

## Configuración local

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd dsy1105-pasteleria-backend
```

### 2. Configurar la base de datos

El proyecto está configurado para usar Oracle Autonomous Database. La configuración se encuentra en `src/main/resources/application.properties`.

Por defecto, el proyecto busca la wallet de Oracle en:
```
C:/Users/anyar/projects/Wallet_PasteleriaMoviles
```

Si tu wallet está en otra ubicación, actualiza la variable `spring.datasource.url` en `application.properties`.

### 3. Ejecutar el proyecto

```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints de la API

Base URL: `/api/products`

### Listar todos los productos
```
GET /api/products
```

### Obtener un producto por ID
```
GET /api/products/{id}
```

### Crear un nuevo producto
```
POST /api/products
Content-Type: application/json

{
  "name": "Torta de chocolate",
  "description": "Torta de chocolate con crema",
  "price": 15000.0,
  "stock": 10,
  "imageUrl": "https://ejemplo.com/imagen.jpg"
}
```

### Actualizar un producto
```
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Torta de chocolate",
  "description": "Torta de chocolate con crema actualizada",
  "price": 16000.0,
  "stock": 8,
  "imageUrl": "https://ejemplo.com/imagen.jpg"
}
```

### Eliminar un producto
```
DELETE /api/products/{id}
```

## Estructura del proyecto

```
src/
├── main/
│   ├── java/pasteleria/com/pasteleria/
│   │   ├── config/           # Configuraciones (Oracle Wallet)
│   │   ├── controller/       # Controladores REST
│   │   ├── model/            # Entidades JPA
│   │   ├── repository/       # Repositorios de datos
│   │   ├── service/          # Lógica de negocio
│   │   └── PasteleriaApplication.java
│   └── resources/
│       ├── META-INF/
│       │   └── spring.factories  # Configuración de carga temprana
│       └── application.properties
```

## Despliegue en Railway

El proyecto está configurado para desplegarse automáticamente en Railway cuando se hace push a la rama principal.

### Variables de entorno necesarias en Railway

La aplicación utiliza Oracle Wallet codificada en Base64 para conectarse a la base de datos. Necesitas configurar las siguientes variables de entorno:

```
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@pasteleriamoviles_high?TNS_ADMIN=/tmp/oracle_wallet
SPRING_DATASOURCE_USERNAME=ADMIN
SPRING_DATASOURCE_PASSWORD=<tu-password>
ORACLE_WALLET_CWALLET_SSO=<base64-encoded>
ORACLE_WALLET_EWALLET_P12=<base64-encoded>
ORACLE_WALLET_EWALLET_PEM=<base64-encoded>
ORACLE_WALLET_KEYSTORE_JKS=<base64-encoded>
ORACLE_WALLET_OJDBC_PROPERTIES=<base64-encoded>
ORACLE_WALLET_SQLNET_ORA=<base64-encoded>
ORACLE_WALLET_TNSNAMES_ORA=<base64-encoded>
ORACLE_WALLET_TRUSTSTORE_JKS=<base64-encoded>
```

Para generar las variables de entorno en Base64, ejecuta:

```bash
bash encode-wallet.sh
```

Esto generará un archivo `railway-env-variables.txt` con todas las variables que necesitas copiar a Railway.

## Modelo de datos

### Product

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer | Identificador único (generado automáticamente) |
| name | String | Nombre del producto (máx. 100 caracteres) |
| description | String | Descripción del producto (máx. 1000 caracteres) |
| price | Double | Precio del producto |
| stock | Integer | Cantidad disponible en stock |
| imageUrl | String | URL de la imagen del producto (máx. 500 caracteres) |

## Solución de problemas

### Error: "ORA-12263: Failed to access tnsnames.ora"

Este error indica que la wallet de Oracle no se está cargando correctamente.

**Solución:**
1. Verifica que la wallet esté en la ubicación correcta
2. Si estás en Railway, verifica que todas las variables de entorno estén configuradas
3. Revisa los logs de inicio para ver si `OracleWalletConfig` se ejecutó correctamente

### Error al conectar a la base de datos localmente

**Solución:**
1. Verifica que la wallet esté descargada y en la ruta correcta
2. Verifica que el usuario y contraseña en `application.properties` sean correctos
3. Asegúrate de tener conexión a internet (Oracle ADB está en la nube)

## Notas de seguridad

- La wallet de Oracle **NO** debe subirse a GitHub
- Las credenciales de la base de datos deben manejarse como variables de entorno
- El archivo `.gitignore` está configurado para excluir archivos sensibles