SCRIPTS_DIR := ./scripts

.PHONY: build-dashboard build-backend local-run deploy destroy

build-dashboard:
	chmod +x $(SCRIPTS_DIR)/build-dashboard.sh
	$(SCRIPTS_DIR)/build-dashboard.sh

build-backend: build-dashboard
	chmod +x $(SCRIPTS_DIR)/build-backend.sh
	$(SCRIPTS_DIR)/build-backend.sh

local-run:
	docker compose -f infra/docker/docker-compose.yml up --build

deploy: build-backend
	chmod +x $(SCRIPTS_DIR)/deploy.sh
	$(SCRIPTS_DIR)/deploy.sh

destroy:
	chmod +x $(SCRIPTS_DIR)/destroy.sh
	$(SCRIPTS_DIR)/destroy.sh