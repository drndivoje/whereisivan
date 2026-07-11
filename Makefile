SCRIPTS_DIR := ./scripts

.PHONY: build-dashboard build-backend local-run build-docker-image push-image deploy-ecr deploy-app deploy redeploy destroy-app destroy-ecr destroy

build-dashboard:
	chmod +x $(SCRIPTS_DIR)/build-dashboard.sh
	$(SCRIPTS_DIR)/build-dashboard.sh

build-backend: build-dashboard
	chmod +x $(SCRIPTS_DIR)/build-backend.sh
	$(SCRIPTS_DIR)/build-backend.sh

local-run:
	docker compose -f infra/docker/docker-compose.yml up --build

build-docker-image:
	chmod +x $(SCRIPTS_DIR)/build-docker-image.sh
	$(SCRIPTS_DIR)/build-docker-image.sh

push-image:
	chmod +x $(SCRIPTS_DIR)/build-and-push-ecr.sh
	$(SCRIPTS_DIR)/build-and-push-ecr.sh

deploy-ecr:
	chmod +x $(SCRIPTS_DIR)/deploy.sh
	$(SCRIPTS_DIR)/deploy.sh ecr

deploy-app:
	chmod +x $(SCRIPTS_DIR)/deploy.sh
	$(SCRIPTS_DIR)/deploy.sh app

# Provisions the ECR repo, builds+pushes the arm64 image, then provisions/updates the EC2 instance.
deploy: deploy-ecr push-image deploy-app

# Pushes a fresh :latest image and replaces the running EC2 instance so it picks it up.
redeploy: push-image
	chmod +x $(SCRIPTS_DIR)/redeploy-app.sh
	$(SCRIPTS_DIR)/redeploy-app.sh

destroy-app:
	chmod +x $(SCRIPTS_DIR)/destroy.sh
	$(SCRIPTS_DIR)/destroy.sh app

destroy-ecr:
	chmod +x $(SCRIPTS_DIR)/destroy.sh
	$(SCRIPTS_DIR)/destroy.sh ecr

destroy: destroy-app destroy-ecr
