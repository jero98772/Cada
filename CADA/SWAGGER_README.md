# Documentación de Swagger - CADA

## Descripción

Este proyecto ahora incluye documentación completa de la API REST utilizando **Swagger/OpenAPI 3.0**. La documentación está implementada utilizando SpringDoc OpenAPI v2.3.0.

## Tecnologías Utilizadas

- **SpringDoc OpenAPI**: 2.3.0
- **Spring Boot**: 3.5.5
- **Java**: 17
- **Swagger UI**: 5.10.3

## Estructura de la Implementación

### 1. Dependencias Agregadas

En el `pom.xml` se agregó:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. Configuración

#### OpenApiConfig.java
Ubicación: `src/main/java/com/example/CADA/config/OpenApiConfig.java`

Contiene la configuración general de la API:
- Título: "CADA - Sistema de Gestión de Torneos y Árbitros"
- Versión: 1.0.0
- Descripción de la API
- Información de contacto
- Licencia Apache 2.0

#### SecurityConfig.java
Se modificó para permitir acceso sin autenticación a:
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/swagger-ui.html`
- `/api/**` (endpoints REST)

### 3. Controladores REST

Se crearon tres controladores REST completamente documentados:

#### ArbitroRestController
**Base URL**: `/api/v1/arbitros`

Endpoints disponibles:
- `GET /api/v1/arbitros` - Obtener todos los árbitros
- `GET /api/v1/arbitros/activos` - Obtener árbitros activos
- `GET /api/v1/arbitros/{id}` - Obtener árbitro por ID
- `POST /api/v1/arbitros` - Crear nuevo árbitro
- `PUT /api/v1/arbitros/{id}` - Actualizar árbitro
- `DELETE /api/v1/arbitros/{id}` - Eliminar árbitro

#### PartidoRestController
**Base URL**: `/api/v1/partidos`

Endpoints disponibles:
- `GET /api/v1/partidos` - Obtener todos los partidos
- `GET /api/v1/partidos/{id}` - Obtener partido por ID
- `POST /api/v1/partidos` - Crear nuevo partido
- `PUT /api/v1/partidos/{id}` - Actualizar partido
- `DELETE /api/v1/partidos/{id}` - Eliminar partido

#### TorneoRestController
**Base URL**: `/api/v1/torneos`

Endpoints disponibles:
- `GET /api/v1/torneos` - Obtener todos los torneos
- `GET /api/v1/torneos/{id}` - Obtener torneo por ID
- `POST /api/v1/torneos` - Crear nuevo torneo
- `PUT /api/v1/torneos/{id}` - Actualizar torneo
- `DELETE /api/v1/torneos/{id}` - Eliminar torneo

## Cómo Usar Swagger

### 1. Iniciar la Aplicación

```bash
mvn spring-boot:run
```

O compilar y ejecutar:

```bash
mvn clean package
java -jar target/CADA-0.0.1-SNAPSHOT.jar
```

### 2. Acceder a Swagger UI

Una vez que la aplicación esté ejecutándose, abre tu navegador y ve a:

```
http://localhost:8080/swagger-ui.html
```

o

```
http://localhost:8080/swagger-ui/index.html
```

### 3. Acceder a la Documentación OpenAPI JSON

Para obtener la especificación OpenAPI en formato JSON:

```
http://localhost:8080/v3/api-docs
```

Para obtener la especificación OpenAPI en formato YAML:

```
http://localhost:8080/v3/api-docs.yaml
```

## Características de la Documentación

Cada endpoint incluye:

✅ **Descripción detallada** - Qué hace el endpoint
✅ **Parámetros** - Documentación completa de todos los parámetros
✅ **Códigos de respuesta** - Todos los códigos HTTP posibles (200, 201, 400, 404, etc.)
✅ **Esquemas de datos** - Modelos de las entidades (Arbitro, Partido, Torneo)
✅ **Ejemplos de request/response** - Generados automáticamente
✅ **Botón "Try it out"** - Permite probar los endpoints directamente desde la interfaz

## Configuración Personalizada

En `application.properties`:

```properties
# SpringDoc OpenAPI (Swagger) Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.show-actuator=false
```

## Anotaciones Utilizadas

### Nivel de Controlador
- `@Tag` - Agrupa endpoints por controlador
- `@RestController` - Define el controlador REST
- `@RequestMapping` - Define la ruta base

### Nivel de Método
- `@Operation` - Descripción del endpoint
- `@ApiResponses` y `@ApiResponse` - Documenta respuestas HTTP
- `@Parameter` - Documenta parámetros de entrada

### Nivel de Modelo
- Las entidades JPA con `@Entity` se documentan automáticamente
- Los campos se infieren automáticamente de las propiedades

## Ejemplos de Uso

### Crear un Nuevo Árbitro

```bash
curl -X POST "http://localhost:8080/api/v1/arbitros" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jperez",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "jperez@example.com",
    "especialidad": "FUTBOL_11",
    "escalafon": "PRIMERA",
    "activo": true
  }'
```

### Obtener Todos los Partidos

```bash
curl -X GET "http://localhost:8080/api/v1/partidos"
```

### Actualizar un Torneo

```bash
curl -X PUT "http://localhost:8080/api/v1/torneos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Liga Nacional 2025",
    "temporada": "2024/2025",
    "fechaInicio": "2025-01-15",
    "fechaFin": "2025-12-20",
    "activo": true
  }'
```

## Seguridad

⚠️ **Importante**: Actualmente los endpoints de la API (`/api/**`) están configurados sin autenticación para facilitar el desarrollo. En producción, se recomienda:

1. Agregar autenticación JWT o similar
2. Implementar control de acceso basado en roles
3. Configurar CORS apropiadamente
4. Limitar el acceso a Swagger UI en producción

## Solución de Problemas

### Swagger UI no carga

1. Verifica que la aplicación esté ejecutándose en el puerto 8080
2. Asegúrate de acceder a `http://localhost:8080/swagger-ui.html`
3. Revisa los logs de la aplicación para errores

### No aparecen los endpoints

1. Verifica que los controladores REST estén en el paquete `com.example.CADA` o subpaquetes
2. Asegúrate de que los controladores tengan la anotación `@RestController`
3. Confirma que SpringDoc está en el classpath

### Error 403 Forbidden

1. Verifica la configuración de seguridad en `SecurityConfig.java`
2. Asegúrate de que las rutas de Swagger estén permitidas

## Próximos Pasos

Para mejorar aún más la documentación:

1. ✅ Agregar anotaciones `@Schema` a las entidades para descripciones más detalladas
2. ✅ Implementar ejemplos personalizados con `@io.swagger.v3.oas.annotations.media.ExampleObject`
3. ✅ Agregar seguridad con JWT y documentarla en Swagger
4. ✅ Crear DTOs específicos para requests/responses
5. ✅ Agregar validación con `@Valid` y documentar las restricciones

## Recursos Adicionales

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)

---

**Desarrollado para el proyecto CADA**
*Sistema de Gestión de Torneos y Árbitros*
