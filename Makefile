SCRIPTS_DIR := ./scripts

.PHONY: build-dashboard build-backend deploy destroy

build-dashboard:
	chmod +x $(SCRIPTS_DIR)/build-dashboard.sh
	$(SCRIPTS_DIR)/build-dashboard.sh

build-backend: build-dashboard
	chmod +x $(SCRIPTS_DIR)/build-backend.sh
	$(SCRIPTS_DIR)/build-backend.sh

deploy: build-backend
	cd infra && terraform apply --auto-approve

destroy:
	cd infra && terraform destroy --auto-approve