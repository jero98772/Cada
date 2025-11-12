# Build Java application
FROM maven:3.9-eclipse-temurin-17 AS java-build
WORKDIR /app
COPY CADA/pom.xml .
COPY CADA/mvnw .
COPY CADA/mvnw.cmd .
COPY CADA/.mvn .mvn
RUN mvn dependency:go-offline -B
COPY CADA/src ./src
RUN mvn clean package -DskipTests

# Build Node.js application
FROM node:20-alpine AS node-build
WORKDIR /app
COPY arbitros-express/package.json arbitros-express/package-lock.json* ./
RUN npm install --no-audit --no-fund
COPY arbitros-express/ .
RUN npm run build

# Java Runtime
FROM eclipse-temurin:17-jre-alpine AS java-runtime
WORKDIR /app
COPY --from=java-build /app/target/CADA-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Node.js Runtime
FROM node:20-alpine AS node-runtime
ENV NODE_ENV=production
WORKDIR /app
COPY arbitros-express/package.json arbitros-express/package-lock.json* ./
RUN npm install --omit=dev --no-audit --no-fund
COPY --from=node-build /app/dist ./dist
EXPOSE 3000
CMD ["node", "dist/server.js"]
