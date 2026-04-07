dev:
	docker compose -f compose.yaml -f compose.dev.yaml up --build

dev-down:
	docker compose -f compose.yaml -f compose.dev.yaml down -v

prod:
	docker compose up --build

prod-down:
	docker compose down 