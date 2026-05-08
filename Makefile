#.PHONY: dep-check dep-check-offline update-nvd
# Load .env automatically when using "make ..."
ifneq (,$(wildcard ./.env))
    include .env
    export
endif


dev:
	docker compose -f compose.yaml -f compose.dev.yaml up --build

dev-down:
	docker compose -f compose.yaml -f compose.dev.yaml down

dev-clean:
	docker compose -f compose.yaml -f compose.dev.yaml down -v --remove-orphans

e2e:
	docker compose -f compose.e2e.yaml up --build -d

e2e-down:
	docker compose -f compose.e2e.yaml down -v

prod:
	docker compose up --build

prod-down:
	docker compose down

prod-clean:
	docker compose down -v

# ─────────────────────────────
# dependency check, start from root of project
# ─────────────────────────────
dep-check:
	cd backend && mvn dependency-check:check\
		-Dnvd.api.key=$(NVD_API_KEY)\
		-Dmaven.repo.local=.m2/repository

dep-check-offline:
	cd backend && mvn dependency-check:check\
		-Dnvd.api.key=$(NVD_API_KEY)\
		-Dmaven.repo.local=.m2/repository -Ddependency-check.skipUpdate=true