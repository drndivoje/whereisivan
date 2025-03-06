output "backend_url" {
  value       = "${module.ec2.ec2_instance_url}:8080"
  description = "The URL of the EC2 instance"
}
