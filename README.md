# Time Deposit API

Kotlin and Spring Boot REST API using hexagonal architecture and PostgreSQL.

## Run with Docker

1. Start the application and database. If `.env.dev` does not exist, `make start`
   creates it with random database credentials and a random password.

```bash
docker compose --env-file .env.dev -f compose.yaml up --build
```

The API is available on `http://localhost:8080` and the OpenAPI UI is at
`http://localhost:8080/swagger-ui.html`.

## Endpoints

```bash
curl http://localhost:8080/api/time-deposits
curl -X PUT http://localhost:8080/api/time-deposits/balances
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
