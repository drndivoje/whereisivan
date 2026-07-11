# Resource: ECR Repository
resource "aws_ecr_repository" "backend" {
  name                 = var.ecr_repository_name
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  tags = merge(
    var.tags,
    {
      Name = "${local.project}-backend"
    }
  )
}
