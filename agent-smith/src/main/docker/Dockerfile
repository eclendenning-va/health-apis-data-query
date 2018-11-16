FROM openjdk:8-jre-slim

MAINTAINER VaApiDevelopmentTeam

# JMeter version
ARG JMETER_VERSION=5.0

# Install few utilities
RUN apt-get clean && \
    apt-get update && \
    apt-get -qy install \
                wget \
                telnet \
                iputils-ping \
                unzip \
                maven

# Install JMeter
RUN   mkdir /jmeter \
      && cd /jmeter/ \
      && wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-$JMETER_VERSION.tgz \
      && tar -xzf apache-jmeter-$JMETER_VERSION.tgz \
      && rm apache-jmeter-$JMETER_VERSION.tgz

# Set JMeter Home
ENV JMETER_HOME /jmeter/apache-jmeter-$JMETER_VERSION/

# Add JMeter to the Path
ENV PATH $JMETER_HOME/bin:$PATH

# Copy required files
COPY /files/Agent-Smith.jmx /tmp/src/test/jmeter/Agent-Smith.jmx
COPY /files/user.properties /tmp/src/test/jmeter/user.properties
COPY /files/pom.xml /tmp/pom.xml

COPY /entrypoint.sh /tmp/entrypoint.sh


# File executable permissions seem to get lost when building the image on windows
RUN chmod 777 /tmp/entrypoint.sh

# Start Up
ENTRYPOINT ["/tmp/entrypoint.sh"]