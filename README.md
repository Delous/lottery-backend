# Lottery Backend

Backend-сервис для простой лотерейной системы. Приложение позволяет регистрировать пользователей, входить по JWT, просматривать активные тиражи, покупать билеты, получать результаты, а администратору - создавать и закрывать тиражи.

## Технологии

- Java 17
- Javalin 6.1.3
- Maven
- PostgreSQL
- JDBC
- JWT
- Password4j
- SLF4J + Logback
- Docker / Docker Compose

## Структура проекта

```text
src/main/java/ru/onexteam/lottery
├── App.java              # точка входа, регистрация роутов и логирование
├── config/               # подключение к БД и инициализация схемы
├── controller/           # HTTP endpoints
├── dto/                  # request/response DTO
├── model/                # доменные модели
├── repository/           # работа с PostgreSQL через JDBC
├── security/             # JWT, пароли, middleware авторизации
└── service/              # бизнес-логика
```

Ресурсы находятся в `src/main/resources`: настройки приложения, OpenAPI-спецификация, HTML-страница документации и конфигурация логирования.

## Запуск через Docker Compose

Требуется установленный Docker. Находясь в корне проекта, 

```bash
docker compose up --build
```

После запуска:

- API: `http://localhost:8080`
- Swagger/OpenAPI документация: `http://localhost:8080/docs`
- OpenAPI JSON: `http://localhost:8080/openapi.json`
- PostgreSQL: `localhost:5432`

Compose поднимает два сервиса: само приложение и PostgreSQL. Схема базы создаётся автоматически при старте приложения.

Данные администратора по умолчанию:

```text
email: admin@lottery.local
password: admin
```

## Локальный запуск без Docker

Нужны Java 17, Maven и запущенный PostgreSQL.

Настройки подключения лежат в `src/main/resources/application.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/lottery_db
db.username=postgres
db.password=postgres
```

Сборка и запуск:

```bash
mvn -DskipTests package
java -jar target/lottery-backend-1.0-SNAPSHOT.jar
```

Настройки можно переопределить переменными окружения:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
APP_ADMIN_EMAIL
APP_ADMIN_PASSWORD
```

## Основные endpoints

- `POST /api/auth/register` - регистрация пользователя
- `POST /api/auth/login` - вход и получение JWT
- `GET /api/user/draws` - список активных тиражей
- `GET /api/user/draws/{drawId}` - информация о тираже
- `POST /api/user/draws/{drawId}/tickets` - покупка билета
- `GET /api/user/me/tickets` - билеты текущего пользователя
- `GET /api/user/me/tickets/{ticketId}/result` - результат билета
- `POST /api/admin/draws` - создание тиража
- `POST /api/admin/draws/{drawId}/close` - закрытие тиража и расчёт результата

Для `/api/user/*` и `/api/admin/*` нужен заголовок авторизации:

```text
Authorization: Bearer <token>
```

## Логи

Логи пишутся в консоль и в директорию `logs/`. Access log показывает метод, фактический путь, HTTP-статус, время обработки и текст ошибки для ответов `4xx/5xx`.
j