# define Cluster
resource "aws_ecs_cluster" "ecs_cluster" {
  name = "${var.name_prefix}-cluster"
}

# define Task

resource "aws_ecs_task_definition" "ecs_task" {
  family                = "${var.name_prefix}-task"
  execution_role_arn    = aws_iam_role.ecs_service_role.arn
  network_mode          = "awsvpc"
  cpu                   = "${var.fargate_cpu}"
  memory                = "${var.fargate_memory}"
  container_definitions = templatefile("container-definition-template.tftpl", {
    app_image      = var.app_image
    fargate_cpu    = var.fargate_cpu
    fargate_memory = var.fargate_memory
    aws_region     = var.aws_region
    app_port       = var.container_port
  })
}

# define Service
resource "aws_ecs_service" "ecs_service" {
  name            = "${var.name_prefix}-backend"
  cluster         = "${aws_ecs_cluster.ecs_cluster.id}"
  task_definition = "${aws_ecs_task_definition.ecs_task.arn}"
  desired_count   = "${var.min_capacity}"

  network_configuration {
    security_groups = ["${aws_security_group.ecs_tasks_sg.id}"]

    subnets = [for i in aws_subnet.private_subnet[*] : i.id]
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.alb_target_group.id}"
    container_name   = "${var.balanced_container_name}"
    container_port   = "${var.container_port}"
  }

  depends_on = [aws_alb_listener.load_balancer_listener]
}

resource "aws_ecs_cluster_capacity_providers" "example" {
  cluster_name = aws_ecs_cluster.ecs_cluster.name

  capacity_providers = [aws_ecs_capacity_provider.ecs_az1_capacity_provider.name]
  default_capacity_provider_strategy {
    base              = 1
    weight            = 100
    capacity_provider = aws_ecs_capacity_provider.ecs_az1_capacity_provider.name
  }
}

resource "aws_ecs_capacity_provider" "ecs_az1_capacity_provider" {
  name = "${var.name_prefix}-az1"
  auto_scaling_group_provider {
    auto_scaling_group_arn         = aws_autoscaling_group.ecs_asg.arn
    managed_termination_protection = "ENABLED"
    managed_scaling {
      status                    = "ENABLED"
      target_capacity           = 1
      maximum_scaling_step_size = 1
    }
  }
}