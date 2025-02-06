output "iam_instance_profile" {
  description = "The IAM instance profile attached to the EC2 instance."
  value       = aws_iam_instance_profile.ec2_instance_profile.name  
}
