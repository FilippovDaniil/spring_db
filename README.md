# Spring DB — Финансовый сервис управления счетами

REST API на Spring Boot для управления аккаунтами и финансовыми операциями: пополнение, списание и перевод средств между счетами.

## Технологии

- **Java 8**
- **Spring Boot 2.1.2**
- **Spring Data JPA / Hibernate**
- **PostgreSQL** (production)
- **H2** (тесты)
- **Lombok**
- **Maven**

## Структура проекта

```
src/main/java/Start/
├── Application.java
├── controller/
│   ├── AccountController.java
│   ├── DepositController.java
│   ├── PaymentController.java
│   ├── TransferController.java
│   └── dto/
├── entity/
│   ├── Account.java
│   └── Bill.java
├── repository/
│   └── AccountRepository.java
├── service/
│   ├── AccountService.java
│   ├── DepositService.java
│   ├── PaymentService.java
│   └── TransferService.java
├── exceptions/
└── utils/
```

## Доменная модель

**Account** — аккаунт пользователя (имя, email, список счетов).  
**Bill** — счёт аккаунта (сумма, флаг `isDefault`).

Все финансовые операции выполняются через **счёт по умолчанию** (`isDefault = true`).

## API

### Аккаунты

| Метод | URL | Описание |
|-------|-----|----------|
| `POST` | `/accounts` | Создать аккаунт |
| `GET` | `/accounts/{id}` | Получить аккаунт по ID |

**Создать аккаунт** `POST /accounts`
```json
{
  "name": "Иван",
  "email": "ivan@example.com",
  "bills": [
    { "amount": 1000.0, "isDefault": true }
  ]
}
```

**Ответ:**
```json
{
  "id": 1,
  "name": "Иван",
  "email": "ivan@example.com",
  "bills": [
    { "id": 1, "amount": 1000.0, "isDefault": true }
  ]
}
```

---

### Пополнение `POST /deposit`
```json
{
  "account_id": 1,
  "amount": 500.0
}
```

### Списание `POST /payments`
```json
{
  "account_id": 1,
  "amount": 200.0
}
```

### Перевод `POST /transfers`
```json
{
  "account_id_from": 1,
  "account_id_to": 2,
  "amount": 300.0
}
```

## Запуск

### Через Docker Compose (рекомендуется)

```bash
docker-compose up --build
```

Приложение запустится на `http://localhost:9999`, база данных PostgreSQL поднимется автоматически.

### Локально

1. Запустить PostgreSQL и создать базу данных:
```sql
CREATE DATABASE spring_db;
```

2. Проверить настройки в `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/spring_db
    username: postgres
    password: 1234
```

3. Собрать и запустить:
```bash
mvn spring-boot:run
```

## Тесты

Тесты используют H2 in-memory базу данных и не требуют PostgreSQL.

```bash
mvn test
```

Покрытие включает:
- `AccountServiceTest` — CRUD операции с аккаунтами
- `DepositServiceTest` — пополнение счёта
- `PaymentServiceTest` — списание со счёта
- `TransferServiceTest` — перевод между аккаунтами
- `AccountControllerTest` — интеграционные тесты REST-контроллера

## Конфигурация

| Параметр | Значение по умолчанию |
|----------|----------------------|
| Порт сервера | `9999` |
| БД (prod) | `postgresql://localhost:5432/spring_db` |
| Пользователь БД | `postgres` |
| Пароль БД | `1234` |
| DDL стратегия | `update` |

## Postman

Коллекция для ручного тестирования: `postman_tests/spring_db_collection.json`

Импортируйте файл в Postman и выполняйте запросы к `http://localhost:9999`.
