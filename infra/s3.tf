# Resource: S3 Bucket
resource "aws_s3_bucket" "bucket" {
  bucket = "whereisivan-bucket"
  tags = merge(
    var.tags,
    {
      Name = "${local.project}-bucket"
    }
  )

}

# Resource: S3 Bucket Object
resource "aws_s3_object" "backend_jar" {
  bucket = aws_s3_bucket.bucket.bucket
  key    = "app.jar"
  source = var.backend_jar_path
  acl    = "private"
}

# IAM Policy for S3 Bucket Access
resource "aws_iam_policy" "s3_bucket_policy" {
  name        = "s3-bucket-policy"
  description = "Policy to allow access to specific S3 bucket"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:ListBucket",
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject"
        ]
        Resource = [
          aws_s3_bucket.bucket.arn,
          "${aws_s3_bucket.bucket.arn}/*"
        ]
      }
    ]
  })
}

# IAM Role Policy Attachment
resource "aws_iam_role_policy_attachment" "s3_access_attachment" {
  role       = module.ec2.iam_role
  policy_arn = aws_iam_policy.s3_bucket_policy.arn
}





