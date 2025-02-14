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





