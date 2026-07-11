# IAM Policy for ECR Pull Access
resource "aws_iam_policy" "ecr_pull_policy" {
  name        = "ecr-pull-policy"
  description = "Policy to allow the EC2 instance to pull the backend image from ECR"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = ["ecr:GetAuthorizationToken"]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchCheckLayerAvailability"
        ]
        Resource = data.aws_ecr_repository.backend.arn
      }
    ]
  })
}

# IAM Role Policy Attachment
resource "aws_iam_role_policy_attachment" "ecr_pull_attachment" {
  role       = module.ec2.iam_role
  policy_arn = aws_iam_policy.ecr_pull_policy.arn
}
