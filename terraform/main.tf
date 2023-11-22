
provider "aws" { }

resource "aws_instance" "ai_web_application" {
  ami           = "ami-0ac5ee2301d3a694c" // amazon linux2 with gpu 
  instance_type = "g4dn.xlarge"

  key_name = aws_key_pair.cicd_make_keypair.key_name
  vpc_security_group_ids = [aws_security_group.cicd_sg.id]
  associate_public_ip_address = true

  user_data = file("setup.sh")
}

resource "tls_private_key" "cicd_make_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "cicd_make_keypair" {
  key_name   = "cicd_key"
  public_key = tls_private_key.cicd_make_key.public_key_openssh
}

resource "local_file" "cicd_downloads_key" {
  filename = "cicd_key.pem"
  content  = tls_private_key.cicd_make_key.private_key_pem
}


// security 
resource "aws_security_group" "cicd_sg" {
  name_prefix = "cicd-sg"
}

resource "aws_security_group_rule" "cicd_sg_ingress_ssh" {
  type        = "ingress"
  from_port   = 22
  to_port     = 22
  protocol    = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.cicd_sg.id
}

resource "aws_security_group_rule" "cicd_sg_ingress_https" {
  type        = "ingress"
  from_port   = 8080
  to_port     = 8090
  protocol    = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  security_group_id = aws_security_group.cicd_sg.id
}

resource "aws_security_group_rule" "cicd_sg_egress_all" {
  type             = "egress"
  from_port        = 0
  to_port          = 0
  protocol         = "-1"
  cidr_blocks      = ["0.0.0.0/0"]
  security_group_id = aws_security_group.cicd_sg.id
}