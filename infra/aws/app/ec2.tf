data "aws_vpc" "default" {
  default = true
}

data "aws_caller_identity" "current" {}

data "aws_ecr_repository" "backend" {
  name = var.ecr_repository_name
}

resource "aws_security_group" "backend_api" {
  name   = "${local.project}-sg"
  vpc_id = data.aws_vpc.default.id

  ingress {
    protocol        = "tcp"
    from_port       = 22
    to_port         = 22
    prefix_list_ids = ["pl-03384955215625250"]
  }
  ingress {
    protocol    = "tcp"
    from_port   = 8080
    to_port     = 8080
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

module "ec2" {
  source            = "git::https://github.com/drndivoje/terraform-modules.git//minimal-ec2"
  instance_name     = "${local.project}-backend"
  vpc_id            = data.aws_vpc.default.id
  security_group_id = aws_security_group.backend_api.id
  operating_system  = "amazon_linux"
  user_data         = <<-EOT
    sudo yum install -y jq unzip
    sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
    sudo systemctl enable amazon-ssm-agent
    sudo systemctl start amazon-ssm-agent
    sudo amazon-linux-extras install docker -y
    sudo systemctl enable docker
    sudo systemctl start docker
    sudo yum remove awscli -y
    curl -O 'https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip'
    unzip awscli-exe-linux-aarch64.zip
    sudo ./aws/install
    aws ecr get-login-password --region ${var.aws_region} | sudo docker login --username AWS --password-stdin ${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.aws_region}.amazonaws.com
    sudo docker run -d --name whereisivan --restart unless-stopped -p 8080:8080 ${data.aws_ecr_repository.backend.repository_url}:${var.image_tag}
  EOT
}


