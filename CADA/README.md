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

## Technical Notes

* Security: Spring Security with in-memory users and roles `ROLE_ADMIN` and `ROLE_ARBITRO`.
* Views: Thymeleaf + Bootstrap 5 via CDN; `thymeleaf-extras-springsecurity6` for `sec:authorize` directives.
* Data: in-memory services (ConcurrentHashMap) + `MockDataLoader` for seeding data.
* Config: `spring.thymeleaf.cache=false` to ease development.

## Reminder

In the JDBC URL field, change it from:

```
jdbc:h2:~/test
```

to:

```
jdbc:h2:file:./data/cada-db
```

