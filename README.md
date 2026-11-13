# Time Deposit API

Kotlin and Spring Boot REST API using hexagonal architecture and PostgreSQL.

## Prerequisites

- Java 17 or newer
- Maven 3.9 or newer
- Docker Desktop with Docker Compose
- A running Docker daemon for Testcontainers integration tests

## Build And Test

Run the complete test suite, including PostgreSQL Testcontainers tests:

```bash
mvn clean test
```

## Run with Docker

For the first startup, use `make start`. It creates `.env.dev` with random
development credentials when the file does not already exist, then builds and
starts the application and PostgreSQL containers:

```bash
make start
```

The direct Compose command is also available after `.env.dev` exists:

```bash
docker compose --env-file .env.dev -f compose.yaml up --build
```

The API is available on `http://localhost:8080` and the OpenAPI UI is at
`http://localhost:8080/swagger-ui.html`.

## Swagger UI

1. Start the application with `make start`.
2. Open `http://localhost:8080/swagger-ui.html`.
3. Expand `GET /api/time-deposits`, select **Try it out**, and select
   **Execute** to retrieve all deposits and their withdrawals.
4. Expand `PUT /api/time-deposits/balances`, select **Try it out**, and select
   **Execute**. This endpoint has no request body and returns HTTP `204` after
   persisting updated balances.

The generated OpenAPI document is available at
`http://localhost:8080/v3/api-docs`.

## API Examples

The two application endpoints can also be called directly:

```bash
curl http://localhost:8080/api/time-deposits
curl -X PUT http://localhost:8080/api/time-deposits/balances
```

Example response from `GET /api/time-deposits`:

```json
[
  {
    "id": 1,
    "planType": "basic",
    "balance": 1200.0,
    "days": 60,
    "withdrawals": [
      {
        "id": 1,
        "amount": 25.50,
        "date": "2026-01-15"
      }
    ]
  }
]
```

PostgreSQL data is stored in the `postgres_data` Docker volume. The `.env.dev`
file is ignored by Git and is the only environment file used by this project.

## Make commands
| Command                            | Description                                                                                                     |
|------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| `make start`                       | Creates `.env.dev` with random credentials if needed, then builds and starts the app and PostgreSQL containers. |
| `make stop`                        | Stops and removes the app and PostgreSQL containers while preserving the database volume.                       |
| `make restart`                     | Stops and starts the containers again using the existing `.env.dev`.                                            |
| `make logs`                        | Follows live logs from the application container. Press `Ctrl+C` to stop following logs.                        |
| `make connectdb`                   | Opens an interactive `psql` session inside the PostgreSQL container.                                            |
| `make migration NAME=add_a_column` | Creates a timestamped Flyway migration file under `src/main/resources/db/migration`.                            |

The generated `.env.dev` is ignored by Git and is reused on later starts so the
credentials remain compatible with the persistent PostgreSQL volume.

The Compose file is `compose.yaml` and the local project instructions are in `AGENTS.md`.

## Architecture And Assumptions

- Core domain code (`TimeDeposit` and `TimeDepositCalculator`) remains independent of Spring and persistence.
- `application` contains the service orchestration, read model, and repository port.
- `adapter/web` exposes the REST contract and maps application read models to API DTOs.
- `adapter/persistence` contains JPA entities, the Spring Data repository, and the repository-port adapter.
- Flyway owns database schema creation; Hibernate runs with schema validation rather than creating tables.
- The original four-property `TimeDeposit` data class and `updateBalance(List<TimeDeposit>)` signature are preserved.
- Withdrawals are persistence/read-model/API projection data and are not added to the original domain data class.
- Interest is applied once per update invocation, rounded to two decimals using `HALF_UP`, and unknown plans remain unchanged.
- The assignment’s logical names are mapped to PostgreSQL `snake_case` physical names as documented below.
- Exactly two application endpoints are exposed: GET all deposits and PUT all balances.

## Database Naming Assumption
The assignment’s logical names are `timeDeposits` and `timeDepositId`; the database uses PostgreSQL-standard `snake_case` (`time_deposits`, `time_deposit_id`) consistently in Flyway and JPA, avoiding quoted case-sensitive identifiers.

## AI-Assisted Development

I used OpenCode with the `gpt-5.6-luna` model as a development assistant. I reviewed the requirements, directed the implementation decisions, inspected generated changes, and validated the result locally.

AI assisted with scaffolding, refactoring, test generation, configuration, and documentation. All changes were reviewed, and the build and tests were run locally before committing.
