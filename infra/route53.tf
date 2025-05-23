locals {
  domain_name = "gps.${var.route53_zone}"
}
data "aws_route53_zone" "main" {
  name = var.route53_zone
}

resource "aws_route53_record" "subdomain" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = local.domain_name
  type    = "A"
  ttl     = 300
  records = [module.ec2.instance_public_ip]
}