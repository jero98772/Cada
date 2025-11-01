# Árbitros API (Express)

API REST en Node.js/Express para árbitros (dashboards, partidos, liquidaciones,
autenticación y registro). Esta API actúa como BFF/Proxy y consume la API de
Spring Boot del Entregable 2. Las imágenes se suben a S3 con acceso público.

## Requisitos
- Node 18+
- API Spring Boot en ejecución (URL base en `SPRING_API_BASE_URL`)
- Bucket S3 con objetos públicos (ver sección S3)

## Configuración
1. Copia `.env.example` a `.env` y ajusta variables:
```
PORT=3000
SPRING_API_BASE_URL=http://localhost:8080/api
AWS_REGION=us-east-1
S3_BUCKET=your-arbitros-bucket
AWS_ACCESS_KEY_ID=your_key
AWS_SECRET_ACCESS_KEY=your_secret
```
2. Instala dependencias y arranca:
```
npm install
npm run dev
```
Docs Swagger: http://localhost:3000/docs
Health: http://localhost:3000/health

## Endpoints clave
- POST `/api/auth/register`, POST `/api/auth/login`, GET `/api/auth/me`
- GET `/api/arbitros`, GET `/api/arbitros/:id`
- POST `/api/arbitros`, PUT `/api/arbitros/:id`
- GET `/api/arbitros/:id/partidos`, GET `/api/arbitros/:id/liquidaciones`
- POST `/api/uploads/avatar` (form-data `file`)
- GET `/api/arbitros/:id/dashboard`

## S3 público
- Habilita acceso público a objetos del bucket (ACL `public-read`).
- Política ejemplo (ajusta `BUCKET_NAME`):
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::BUCKET_NAME/*"
    }
  ]
}
```

## Seed (7-10 árbitros)
```
npm run seed
```
El script sube imágenes placeholder a S3 y crea árbitros vía la API de Spring.
Ajusta el payload si tu esquema difiere.

## Docker
- Build local: `docker build -t arbitros-express-api .`
- Run: `docker run -p 3000:3000 --env-file .env arbitros-express-api`

## CI/CD a DockerHub
Incluye workflow en `.github/workflows/docker-publish.yml`.
Configura secretos en GitHub:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `IMAGE_NAME` (opcional, ej: `alejo/arbitros-express-api`)

## Arquitectura
- `routes/` → rutas HTTP
- `controllers/` → orquestación por endpoint
- `services/` → cliente Spring y lógica
- `middleware/` → auth y errores
- `config/` → env y swagger
- `scripts/seed.ts` → datos iniciales

## Notas
- Esta API no implementa lógicas de administrador, solo árbitros.
- Se reenvía el header `Authorization` a Spring para validación centralizada.
