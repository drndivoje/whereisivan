# Search for ami id
data "aws_ami" "ecs_ami" {
  most_recent = true
  owners      = ["amazon"]

  # Amazon Linux 2 optimised ECS instance
  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm-*"]
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

resource "aws_key_pair" "deployer" {
  key_name   = "${local.project}-backend-kp"
  public_key = file(var.public_key_path)
}

resource "aws_security_group" "network-security-group" {
  vpc_id      = aws_vpc.main_network.id
  name        = "${local.project}-backend-sg"
  description = "Allow TLS inbound traffic"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    description = "Access to Internet"
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "${local.project}-backend-sg"
  }
}
# Host Launch Template
resource "aws_launch_template" "ecs_host_lt" {
  name_prefix          = "${var.name_prefix}-asg"
  image_id             = data.aws_ami.ecs_ami.id
  instance_type        = "m5.xlarge"
  vpc_security_group_ids = [aws_security_group.network-security-group.id]
  user_data            = base64encode(data.template_file.ecs_host_userdata_template.rendered)
  key_name             = aws_key_pair.deployer.key_name
  lifecycle {
    create_before_destroy = true
  }
}
# Host userdata template
data "template_file" "ecs_host_userdata_template" {
  template = file("${path.module}/templates/ecs-host-userdata.tpl")

  vars = {
    ecs_cluster_name = "${var.name_prefix}-cluster"
    region           = var.aws_region
    # efs_sg           = local.ecs_security_groups["efs"]
    log_group_name   = aws_cloudwatch_log_group.log_group.name
    #kms_key_arn      = local.kms_key_arn
  }
}