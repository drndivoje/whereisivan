# Input: VPC ID
variable "vpc_id" {
  description = "The ID of the VPC where the EC2 instance will be deployed."
  type        = string
}

# Input: Subnet ID
variable "subnet_id" {
  description = "The ID of the subnet where the EC2 instance will be deployed."
  type        = string
}

# Input: Security Group ID
variable "security_group_id" {
  description = "Security group ID to attach to the EC2 instance."
  type        = string
}

# Input: Instance Name
variable "instance_name" {
  description = "Name tag for the EC2 instance."
  type        = string
  default     = "docker-instance"
}

# Input: Instance Type
variable "instance_type" {
  description = "The EC2 instance type."
  type        = string
  default     = "t2.micro"
}

# Input: Associate Public IP
variable "associate_public_ip" {
  description = "Whether to associate a public IP address with the instance."
  type        = bool
  default     = true
}

variable "local_file_path" {
  description = "Path to the local file to copy to the EBS volume."
  type        = string
  
}

# Input: Docker Install Script
variable "docker_install_script" {
  description = "Shell script to install Docker on the instance."
  type        = string
  default     = <<-EOT
    #!/bin/bash
    sudo yum update -y
    sudo amazon-linux-extras install docker -y
    sudo service docker start
    sudo usermod -aG docker ec2-user
    sudo chkconfig docker on
  EOT
}

# Input: Tags
variable "tags" {
  description = "A map of tags to assign to the resources."
  type        = map(string)
  default     = {}
}
