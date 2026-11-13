.PHONY: start stop restart resetdb logs connectdb migration ensure-env

start: ensure-env
	docker compose --env-file .env.dev -f compose.yaml up -d --build

ensure-env:
	@if [ -f .env.dev ]; then \
		echo "Using existing .env.dev"; \
	else \
		umask 077; \
		printf 'POSTGRES_DB=time_deposit_%s\nPOSTGRES_USER=time_deposit_app_%s\nPOSTGRES_PASSWORD=%s\nAPP_PORT=8080\n' \
			"$$(openssl rand -hex 4)" "$$(openssl rand -hex 4)" "$$(openssl rand -hex 24)" > .env.dev; \
		echo "Created random .env.dev"; \
	fi

stop:
	docker compose --env-file .env.dev -f compose.yaml down

restart: stop start

resetdb:
	@echo "Resetting PostgreSQL data volume; all local database data will be deleted."
	docker compose --env-file .env.dev -f compose.yaml down -v
	$(MAKE) start

logs:
	docker compose --env-file .env.dev -f compose.yaml logs -f app

connectdb:
	docker compose --env-file .env.dev -f compose.yaml exec db sh -c 'psql -U "$$POSTGRES_USER" -d "$$POSTGRES_DB"'

migration:
	@test -n "$(NAME)" || (echo "Usage: make migration NAME=add_example" && exit 1)
	@mkdir -p src/main/resources/db/migration
	@file="src/main/resources/db/migration/V$$(date +%Y%m%d%H%M%S)__$(NAME).sql"; \
	if [ -e "$$file" ]; then echo "Migration already exists: $$file"; exit 1; fi; \
	printf '%s\n' '-- Describe the schema change here.' > "$$file"; \
	echo "Created $$file"
