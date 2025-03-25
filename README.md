# Personal Bicycle Tracking App


A personal bicycle tracking app built to monitor real-time location data while cycling, allowing users to share their live location with family and friends through a web-based dashboard. This project is a work in progress and serves as a learning platform to enhance my full-stack development skills, while also gaining experience with the Kotlin ecosystem.
## Project Structure

The repository is organized into the following four main folders:
- backend: Contains the server-side API written in Kotlin using the Ktor framework. This handles incoming data from the Android client and serves the API for the dashboard.
- android-client: The Android client application, built with Jetpack Compose, which tracks the user's real-time location and sends it to the backend.
- dashboard: A React web application that visualizes the current location data on a map, allowing users to track the cyclist's activity in real-time.
- infra: Contains Terraform scripts for creating and managing infrastructure on AWS. This deploys the backend API to EC2 instance
- test-client: Command line utillity program which takes gpx file as input and the url of the backend API. It simulates the behavior of the client. Usefull for local host testing

## Features

- Real-time location tracking: Tracks and sends GPS data from the Android client while cycling.
- Web Dashboard: Displays live location data on a map via the React-based dashboard.
- Cloud Deployment: Infrastructure as Code (IaC) using Terraform to manage deployment on AWS.
- Scalable Architecture: Backend and dashboard are containerized with Docker, allowing easy deployment across different environments.

## Technology Stack

- Backend: Kotlin, Ktor
- Android Client: Kotlin, Jetpack Compose
- Frontend Dashboard: React, JavaScript
- Cloud Infrastructure: AWS ECS, Docker, Terraform
