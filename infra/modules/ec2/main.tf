
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
# Search for ami id
data "aws_ami" "amazon" {
  most_recent = true
  owners      = ["amazon"]

  # Amazon Linux 2 optimised ECS instance
  filter {
    name   = "name"
    values = ["al2023-ami-2023.6*"]
  }

  # correct arch
  filter {
    name   = "architecture"
    values = ["x86_64"]
  }

  # Owned by Amazon
  filter {
    name   = "owner-alias"
    values = ["amazon"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

locals {
  ami_id = data.aws_ami.amazon.id
}

# Resource: Launch Template for EC2
resource "aws_launch_template" "this" {
  name_prefix   = "docker-instance"
  description   = "Launch template for Docker-enabled EC2 instance"
  image_id      = local.ami_id
  instance_type = var.instance_type

  user_data = base64encode(templatefile("./${path.module}/templates/userdata.tpl", {
    user_script = var.user_data
  }))

  network_interfaces {
    subnet_id                   = var.subnet_id
    associate_public_ip_address = var.associate_public_ip
    security_groups             = [var.security_group_id]
  }

  tag_specifications {
    resource_type = "instance"
    tags = merge(
      var.tags,
      {
        Name = var.instance_name
      }
    )
  }
}
data "aws_availability_zones" "available" {}

data "aws_subnet" "first" {
  filter {
    name   = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name   = "availability-zone"
    values = [data.aws_availability_zones.available.names[0]]
  }
}

# Resource: EC2 Instance using the Launch Template
resource "aws_instance" "this" {
  launch_template {
    id      = aws_launch_template.this.id
    version = "$Latest"
  }
  iam_instance_profile = var.iam_instance_profile


  tags = merge(
    var.tags,
    {
      Name = var.instance_name
    }
  )
}
