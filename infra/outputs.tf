output "dashboard_url" {
  value       = "http://${local.domain_name}:8080"
  description = "The URL of the Dashboard"
}
