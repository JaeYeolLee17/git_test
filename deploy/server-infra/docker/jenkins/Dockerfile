FROM jenkins/jenkins:lts-jdk11

USER root

RUN groupadd docker

RUN groupmod -g 998 docker

RUN usermod -a -G docker jenkins 

USER jenkins
