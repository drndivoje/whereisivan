# Output: EC2 Instance ID
output "instance_id" {
  description = "The ID of the created EC2 instance."
  value       = aws_instance.docker_instance.id
}

# Output: EC2 Instance Public IP
output "instance_public_ip" {
  description = "The public IP address of the EC2 instance."
  value       = aws_instance.docker_instance.public_ip
}

output "ec2_instance_url" {
    value = "http://${aws_instance.docker_instance.public_dns}"
    description = "The URL of the EC2 instance"
}
