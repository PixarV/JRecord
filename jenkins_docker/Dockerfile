FROM jenkins/jenkins:lts
COPY jdk-11.0.1_linux-x64_bin.deb /jdk-11.0.1_linux-x64_bin.deb
USER root
RUN dpkg -i jdk-11.0.1_linux-x64_bin.deb
RUN rm jdk-11.0.1_linux-x64_bin.deb
USER jenkins
RUN mkdir -p /var/jenkins_home
ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/jenkins.sh"]