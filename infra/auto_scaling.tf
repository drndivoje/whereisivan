# define Autoscaling Target
resource "aws_appautoscaling_target" "autoscaling_target" {
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.ecs_cluster.name}/${aws_ecs_service.ecs_service.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  min_capacity       = "${var.min_capacity}"
  max_capacity       = "${var.max_capacity}"
}

# define Outscaling Policy
resource "aws_appautoscaling_policy" "outscaling_policy" {
  name               = "${var.name_prefix}-outscaling-policy"
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.ecs_cluster.name}/${aws_ecs_service.ecs_service.name}"
  scalable_dimension = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 3
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = 1
    }
  }

  depends_on = [aws_appautoscaling_target.autoscaling_target]
}

resource "aws_cloudwatch_metric_alarm" "outscaling_metric_alarm" {
  alarm_name          = "${var.name_prefix}-outscaling-metric-alarm"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "1"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "60"

  dimensions = {
    ClusterName = "${aws_ecs_cluster.ecs_cluster.name}"
    ServiceName = "${aws_ecs_service.ecs_service.name}"
  }

  alarm_actions = ["${aws_appautoscaling_policy.outscaling_policy.arn}"]
}

# define Downscaling Policy
resource "aws_appautoscaling_policy" "downscaling_policy" {
  name               = "${var.name_prefix}-downscaling-policy"
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.ecs_cluster.name}/${aws_ecs_service.ecs_service.name}"
  scalable_dimension = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 3
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = -1
    }
  }

  depends_on = [aws_appautoscaling_target.autoscaling_target]
}

resource "aws_cloudwatch_metric_alarm" "downscaling_metric_alarm" {
  alarm_name          = "${var.name_prefix}-downscaling-metric-alarm"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods  = "1"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "30"

  dimensions = {
    ClusterName = "${aws_ecs_cluster.ecs_cluster.name}"
    ServiceName = "${aws_ecs_service.ecs_service.name}"
  }

  alarm_actions = ["${aws_appautoscaling_policy.downscaling_policy.arn}"]
}


resource "aws_autoscaling_group" "ecs_asg" {
  name                 = "${var.name_prefix}-host-asg"
  launch_template {
    id      = aws_launch_template.ecs_host_lt.id
    version = "$Latest"
  }

  # Not setting desired count as that could cause scale in when deployment runs and lead to resource exhaustion
  max_size              = 3
  min_size              = 1
  protect_from_scale_in = true # scale-in is managed by ECS
  vpc_zone_identifier   = flatten([aws_subnet.private_subnet[*].id, aws_subnet.public_subnet[*].id])

  enabled_metrics = [
    "GroupMinSize",
    "GroupMaxSize",
    "GroupDesiredCapacity",
    "GroupInServiceInstances",
    "GroupPendingInstances",
    "GroupStandbyInstances",
    "GroupTerminatingInstances",
    "GroupTotalInstances",
  ]

  lifecycle {
    create_before_destroy = true
    ignore_changes        = [desired_capacity]
  }
}


