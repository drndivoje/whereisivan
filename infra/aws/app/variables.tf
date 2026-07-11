variable "ecr_repository_name" {
  description = "Name of the ECR repository holding the backend image (must match infra/aws/ecr)"
  type        = string
  default     = "whereisivan-backend"
}

variable "image_tag" {
  description = "Tag of the backend image to pull from ECR"
  type        = string
  default     = "latest"
}

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

variable "route53_zone" {
  description = "The Route53 hosting zone to use"
  type        = string

}

