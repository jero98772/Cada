# CABA Pro (Frontend MVC con Spring Boot)

Aplicación web para gestión de árbitros y torneos (UI/Frontend) construida con Spring Boot 3, Thymeleaf y Spring Security. Esta iteración usa servicios en memoria (sin base de datos), únicamente para navegar la interfaz.

## Requisitos
- Java 17+
- Maven 3.9+

## Ejecutar
```
mvn spring-boot:run
```
Abrir http://localhost:8080

## Credenciales demo
- Administrador: admin / admin123
- Árbitro: arbitro / arbitro123

## Rutas
- `/login` formulario de autenticación
- `/` redirige según el rol
- Sección Árbitro
  - `/dashboard` listado de asignaciones y acciones aceptar/rechazar
- Sección Admin
  - `/admin` inicio del panel
  - `/admin/arbitros` CRUD de árbitros
  - `/admin/torneos` CRUD de torneos

## Notas técnicas
- Seguridad: Spring Security con usuarios en memoria y roles `ROLE_ADMIN` y `ROLE_ARBITRO`.
- Vistas: Thymeleaf + Bootstrap 5 via CDN; `thymeleaf-extras-springsecurity6` para directivas `sec:authorize`.
- Datos: servicios en memoria (ConcurrentHashMap) + `MockDataLoader` para sembrar datos.
- Config: `spring.thymeleaf.cache=false` para facilitar desarrollo.

## Próximos pasos sugeridos
- Persistencia real con JPA + MySQL y repositorios.
- Calendario visual (FullCalendar) en el dashboard.
- Validaciones con mensajes i18n.
- Tests de controlador con `@WebMvcTest`.

