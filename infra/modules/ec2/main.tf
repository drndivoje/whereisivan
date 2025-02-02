
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
    docker_install_script = var.docker_install_script
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

# Resource: S3 Bucket
resource "aws_s3_bucket" "bucket" {
  bucket = "whereisivan-bucket"
  acl    = "private"

  tags = merge(
    var.tags,
    {
      Name = "whereisivan-bucket"
    }
  )
}
# Resource: S3 Bucket Object
resource "aws_s3_object" "example" {
  bucket = aws_s3_bucket.bucket.bucket
  key    = "app.jar"
  source = var.local_file_path
  acl    = "private"
}

# IAM Role
resource "aws_iam_role" "s3_access_role" {
  name = "s3-access-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# IAM Policy for S3 Bucket Access
resource "aws_iam_policy" "s3_bucket_policy" {
  name        = "s3-bucket-policy"
  description = "Policy to allow access to specific S3 bucket"
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:ListBucket",
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject"
        ]
        Resource = [
          aws_s3_bucket.bucket.arn,
          "${aws_s3_bucket.bucket.arn}/*"
        ]
      }
    ]
  })
}

# IAM Role Policy Attachment
resource "aws_iam_role_policy_attachment" "s3_access_attachment" {
  role       = aws_iam_role.s3_access_role.name
  policy_arn = aws_iam_policy.s3_bucket_policy.arn
}

# IAM Instance Profile
resource "aws_iam_instance_profile" "s3_access_profile" {
  name = "s3-access-profile"
  role = aws_iam_role.s3_access_role.name
}
resource "aws_iam_instance_profile" "ec2_instance_profile" {
  name = "ec2-instance-profile"
  role = aws_iam_role.s3_access_role.name
}


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
resource "aws_instance" "docker_instance" {
  launch_template {
    id      = aws_launch_template.this.id
    version = "$Latest"
  }
  iam_instance_profile = aws_iam_instance_profile.ec2_instance_profile.name


  tags = merge(
    var.tags,
    {
      Name = var.instance_name
    }
  )
}
