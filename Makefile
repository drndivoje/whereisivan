build-dashboard:
	cd dashboard && npm install && npm run build && cp -r build ../backend/src/main/resources/dashboard-app

build-backend: build-dashboard
	cd backend && ./gradlew clean buildFatJar

deploy: build-backend
	cd infra && terraform apply --auto-approve

destroy:
	cd infra && terraform destroy --auto-approve