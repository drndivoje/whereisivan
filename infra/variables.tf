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

variable "healthcheck_path" {
  default = "/"
}

variable "backend_jar_path" {
  description = "Path to backend jar file"
  type        = string
}

variable "tags" {
  description = "A map of tags to assign to the resources."
  type        = map(string)
  default     = {}
}

