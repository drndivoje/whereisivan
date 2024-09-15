# ALB security group
resource "aws_security_group" "load_balancer_sg" {
  name   = "${var.name_prefix}-alb-sg"
  vpc_id = "${aws_vpc.main_network.id}"

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

# ECS tasks security group
resource "aws_security_group" "ecs_tasks_sg" {
  name   = "${var.name_prefix}-ecs-tasks-sg"
  vpc_id = "${aws_vpc.main_network.id}"

  ingress {
    protocol        = "tcp"
    from_port       = 80
    to_port         = 80
    security_groups = ["${aws_security_group.load_balancer_sg.id}"]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# IAM role for a ECS service

data "aws_iam_policy_document" "inline_policy" {
  statement {
    actions = [
      "ecr:GetAuthorizationToken",
      "ecr:BatchCheckLayerAvailability",
      "ecr:GetDownloadUrlForLayer",
      "ecr:BatchGetImage",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_role" "ecs_service_role" {
  name          = "${var.name_prefix}-ecs_service_role"
  inline_policy {
    name   = "policy-8675309"
    policy = data.aws_iam_policy_document.inline_policy.json
  }

  assume_role_policy = data.aws_iam_policy_document.instance_assume_role_policy.json
}

data "aws_iam_policy_document" "instance_assume_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

# ECS Host Role
resource "aws_iam_role" "ecs_host_role" {
  name               = "${var.name_prefix}-host-iam"
  assume_role_policy = data.aws_iam_policy_document.ecs_assume_role_template.json
}
# IAM Templates
data "aws_iam_policy_document" "ecs_assume_role_template" {
  statement {
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}
# ECS Host Profile
resource "aws_iam_instance_profile" "ecs_host_profile" {
  name = "${var.name_prefix}-host-iam"
  role = aws_iam_role.ecs_host_role.name
}

