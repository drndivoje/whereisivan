data "aws_vpc" "default" {
  default = true
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
    sudo yum install -y amazon-efs-utils nfs-utils jq unzip
    sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
    sudo systemctl enable amazon-ssm-agent
    sudo systemctl start amazon-ssm-agent
    wget https://corretto.aws/downloads/latest/amazon-corretto-21-aarch64-linux-jdk.rpm
    sudo yum localinstall amazon-corretto-21-aarch64-linux-jdk.rpm -y
    sudo yum remove awscli -y
    curl -O 'https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip'
    unzip awscli-exe-linux-aarch64.zip
    sudo ./aws/install
    aws s3 cp s3://whereisivan-bucket/app.jar /home/ec2-user/app.jar
    java -jar /home/ec2-user/app.jar > /var/log/app.log 2>&1
  EOT
}


