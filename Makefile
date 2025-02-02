build-backend:
	cd backend && ./gradlew clean buildFatJar

deploy: build-backend
	cd infra && terraform apply --auto-approve

destroy:
	cd infra && terraform destroy --auto-approve