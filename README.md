# CABA Pro (Frontend MVC with Spring Boot)

Web application for referee and tournament management (UI/Frontend) built with Spring Boot 3, Thymeleaf, and Spring Security. This iteration uses in-memory services (no database), only for navigating the interface.

## Requirements

* Java 17+
* Maven 3.9+

## Run

```
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080)

## Demo Credentials

* Administrator: admin / admin123
* Referee: arbitro / arbitro123

## Routes

* `/login` authentication form
* `/` redirects depending on the role
* Referee Section

  * `/dashboard` list of assignments and accept/reject actions
* Admin Section

  * `/admin` admin panel home
  * `/admin/arbitros` CRUD for referees
  * `/admin/torneos` CRUD for tournaments
  * `/admin/partidos` CRUD for Matches
  * `/admin/partidos/1/asignaciones` assigment for referees


## Technical Notes

* Security: Spring Security with in-memory users and roles `ROLE_ADMIN` and `ROLE_ARBITRO`.
* Views: Thymeleaf + Bootstrap 5 via CDN; `thymeleaf-extras-springsecurity6` for `sec:authorize` directives.
* Data: in-memory services (`ConcurrentHashMap`) + `MockDataLoader` for seeding data.
* Config: `spring.thymeleaf.cache=false` to ease development.

## Suggested Next Steps

* Real persistence with JPA + MySQL and repositories.
* Visual calendar (FullCalendar) in the dashboard.
* Validations with i18n messages.
* Controller tests with `@WebMvcTest`.


## Reminder

In the JDBC URL field, change it from:

```
jdbc:h2:~/test
```

to:

```
jdbc:h2:file:./data/cada-db
```


## Run with Docker

### Prerequisites

* Docker and Docker Compose installed

### Build and Run All Services

```bash
# Build and start both Spring Boot API and Express API
docker-compose up --build
```

This will start:
- **Spring Boot API** on [http://localhost:8080](http://localhost:8080)
- **Express API** on [http://localhost:3000](http://localhost:3000)
- **API Documentation** at [http://localhost:3000/docs](http://localhost:3000/docs)

### Stop Services

```bash
docker-compose down
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f spring-api
docker-compose logs -f express-api
```

## Run without Docker

### Spring Boot API

```bash
cd CADA
mvn spring-boot:run
```

### Express API

```bash
cd arbitros-express
npm install
npm run dev
```

Make sure to configure the `.env` file in `arbitros-express/` with the correct `SPRING_API_BASE_URL`.
