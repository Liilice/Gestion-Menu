# 🍽️ Backoffice API – Restaurant Management

## 📌 Description

Ce projet est une API développée avec Spring Boot permettant de gérer les menu d’un restaurant.

L’objectif est de fournir une base solide pour :
- la gestion des menus

Cette API est conçue pour être utilisée par :
- une application web (admin/backoffice)
- une application mobile
- ou tout autre client REST

---

## 🏗️ Architecture

Le projet suit une architecture classique en couches :

Controller → Service → Repository → Database

### 🔹 Controller
Expose les endpoints REST (HTTP)

### 🔹 Service
Contient la logique métier

### 🔹 Repository
Accès aux données via JPA

### 🔹 Entity
Représentation des tables en base de données

---

## ⚙️ Technologies utilisées

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL / H2 (dev)
- Swagger (documentation API)
- Gradle

---

## 🚀 Installation & lancement

### 1. Cloner le projet
```bash
git clone https://github.com/your-repo/backoffice-api.git
cd backoffice-api
```

### 2. Créer un application.properties dans src/main/resources/
```bash
spring.application.name=backoffice

spring.datasource.url=jdbc:postgresql://localhost:DB_PORT/DB_NAME
spring.datasource.username=DB_USER
spring.datasource.password=DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```


### 3. Lancer l'application
```bash
./gradlew bootRun
```
L’API sera disponible sur :
```bash
http://localhost:8080
```
Swagger est disponible ici :
```bash
http://localhost:8080/swagger-ui/index.html
```

### 4. Lancer les test
Lien pour voir les resultats : 
- test de coverage
```bash 
./gradlew clean test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```
- test
```bash 
./gradlew test --rerun-tasks
open build/reports/tests/test/index.html
```