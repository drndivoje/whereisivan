
data "aws_vpc" "default" {
  default = true
}
data "aws_subnets" "all" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }

}

resource "aws_security_group" "backend_api" {
  name   = "${var.name_prefix}-sg"
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
  source            = "./modules/ec2"
  instance_name     = "whereisivan-backend"
  vpc_id            = data.aws_vpc.default.id
  subnet_id         = tolist(data.aws_subnets.all.ids)[0]
  security_group_id = aws_security_group.backend_api.id
  user_data         = <<-EOT
    sudo yum remove awscli -y
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip awscliv2.zip
    sudo ./aws/install
    wget https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.rpm
    sudo yum localinstall amazon-corretto-21-x64-linux-jdk.rpm -y
    aws s3 cp s3://whereisivan-bucket/app.jar /home/ec2-user/app.jar
    java -jar /home/ec2-user/app.jar > /var/log/app.log 2>&1
  EOT
  iam_instance_profile = aws_iam_instance_profile.ec2_instance_profile.name
}


