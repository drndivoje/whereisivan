variable "aws_region" {
  description = "The AWS region to work in"
  type        = string
  default     = "eu-central-1"
}

variable "aws_profile" {
  description = "The AWS CLI profile"
  type        = string
}

variable "tags" {
  description = "A map of tags to assign to the resources."
  type        = map(string)
  default     = {}
}

variable "ecr_repository_name" {
  description = "Name of the ECR repository for the backend image"
  type        = string
  default     = "whereisivan-backend"
}
