variable "backend_jar_path" {
  description = "Path to backend jar file"
  type        = string
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

