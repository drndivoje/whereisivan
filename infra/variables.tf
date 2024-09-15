variable "aws_region" {
  description = "The AWS region to work in"
  type        = string
  default     = "eu-central-1"
}
variable "profile" {
  description = "The AWS CLI profile"
  type        = string
  default     = "terraform"
}

variable "name_prefix" {
  default = "wi"
}


variable "az_count" {
  default = "2"
}

variable "healthcheck_path" {
  default = "/"
}

variable "fargate_cpu" {
  default = "1024"
}

variable "fargate_memory" {
  default = "2048"
}
variable "public_key_path" {
  default = "/home/drnd/.ssh/id_ed25519.pub"
}

variable "min_capacity" {
  default = "1"
}

variable "max_capacity" {
  default = "3"
}

variable "container_port" {
  default = "80"
}

variable "alb_protocol" {
  default = "HTTP"
}

variable "balanced_container_name" {
  default = "backend-api"
}

variable "app_image" {
  default = "588735387325.dkr.ecr.eu-central-1.amazonaws.com/wi:latest"
}