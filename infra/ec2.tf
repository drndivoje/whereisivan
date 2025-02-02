
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
    from_port   = 80
    to_port     = 80
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
  local_file_path   = var.local_file_path
}


