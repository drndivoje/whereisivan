#!/bin/bash
set -x
# Install additional packages
sudo yum install -y amazon-efs-utils nfs-utils jq amazon-cloudwatch-agent unzip
# Install and start SSM Agent service - will always want the latest - used for remote access via aws console/cli
# Avoids need to manage users identity in 2 places and install ansible/dependencies
sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
sudo systemctl enable amazon-ssm-agent
sudo systemctl start amazon-ssm-agent
sudo systemctl enable amazon-cloudwatch-agent
sudo systemctl start amazon-cloudwatch-agent

wget https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.rpm
sudo yum localinstall amazon-corretto-21-x64-linux-jdk.rpm  -y

sudo yum remove awscli -y
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

aws s3 cp s3://whereisivan-bucket/app.jar /home/ec2-user/app.jar
java -jar /home/ec2-user/app.jar > /var/log/app.log 2>&1

