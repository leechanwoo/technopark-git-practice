FROM hashicorp/terraform:1.6

RUN apk update && apk add --no-cache bash 
RUN apt add curl jq aws-cli 

WORKDIR /root 

ENTRYPOINT ["bash"]