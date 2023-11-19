provider "aws" { }


resource "aws_instance" "ai_web_application" {
  ami           = "ami-0ac5ee2301d3a694c" // amazon linux2 with gpu 
  instance_type = "g4dn.xlarge"

  key_name = aws_key_pair.cicd_make_keypair.key_name
  vpc_security_group_ids = [aws_security_group.cicd_sg.id]
  associate_public_ip_address = true

  user_data = file("setup.sh")
}