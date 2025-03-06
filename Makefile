build-dashboard:
	$(shell chmod +x ./scripts/build-dashboard.sh)
	./scripts/build-dashboard.sh

build-backend: build-dashboard
	$(shell chmod +x ./scripts/build-backend.sh)
	./scripts/build-backend.sh

deploy: build-backend
	cd infra && terraform apply --auto-approve

destroy:
	cd infra && terraform destroy --auto-approve