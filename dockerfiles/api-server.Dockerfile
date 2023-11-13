FROM ubuntu:rolling 

RUN apt update && apt upgrade -y 
RUN apt install -y \
    curl \
    zip \
    wget  

RUN curl -s "https://get.sdkman.io" | bash 

SHELL ["/bin/bash", "-c"]
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" \ 
    && sdk install gradle 8.3 \
    && sdk install java 20.0.2-amzn \
    && sdk install springboot 3.1.3 

ENV JAVA_HOME=/root/.sdkman/candidates/java/current
ENV PATH="$PATH:/root/.sdkman/candidates/gradle/current/bin"