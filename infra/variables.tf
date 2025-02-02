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

variable "local_file_path" {
  description = "Path to backend jar file"
  type = string
}
