
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
# get available AZs
data "aws_availability_zones" "available_azs" {}

# define VPC
resource "aws_vpc" "this" {
  cidr_block = "172.17.0.0/16"
  tags = {
    Name = "${var.name}-vpc"
  }
}



resource "aws_subnet" "private_subnet" {
  cidr_block              = "172.17.0.128/25"
  availability_zone       = data.aws_availability_zones.available_azs.names[1]
  vpc_id                  = aws_vpc.this.id
  map_public_ip_on_launch = true
  tags = {
    Name = "${var.name}-private-subnet"
  }
}

resource "aws_subnet" "public_subnet" {
  cidr_block              = "172.17.0.0/25"
  availability_zone       = data.aws_availability_zones.available_azs.names[1]
  vpc_id                  = aws_vpc.this.id
  map_public_ip_on_launch = true
  tags = {
    Name = "${var.name}-public-subnet"
  }
}

# define IGW
resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.this.id
  tags = {
    Name = "${var.name}-igw"
  }
}

resource "aws_route" "internet_access" {
  route_table_id         = aws_vpc.this.main_route_table_id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.internet_gateway.id
}

# define NAT gateway for each private subnet
resource "aws_eip" "nat_gateway_eip" {
  domain     = "vpc"
  depends_on = [aws_internet_gateway.internet_gateway]
  tags = {
    Name = "${var.name}-nat-gateway-eip"
  }
}

resource "aws_nat_gateway" "nat_gateway" {
  subnet_id     = aws_subnet.public_subnet.id
  allocation_id = aws_eip.nat_gateway_eip.id
  tags = {
    Name = "${var.name}-nat-gateway"
  }
}

# define route table for each private subnet
resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.this.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway.id
  }

  tags = {
    Name = "${var.name}-nat-gateway-rt"
  }
}

# associate route tables with private subnets
resource "aws_route_table_association" "private_route_table_association" {
  subnet_id      = aws_subnet.private_subnet.id
  route_table_id = aws_route_table.private_route_table.id
}
