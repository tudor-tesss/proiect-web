# Use an official Ubuntu 20.04 as a parent image
FROM ubuntu:22.10

# Set environment variables for non-interactive installation
ENV DEBIAN_FRONTEND=noninteractive

# Install necessary tools
RUN apt-get update && \
    apt-get install -y wget curl unzip software-properties-common

# Install OpenJDK-19
RUN apt-get -y install libasound2 libc6-i386 libc6-x32 libfreetype6 libx11-6 libxext6 libxi6 libxrender1 libxtst6
RUN /usr/bin/wget https://download.oracle.com/java/19/archive/jdk-19.0.2_linux-x64_bin.deb
RUN dpkg -i jdk-19.0.2_linux-x64_bin.deb
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-19/bin/java 1919

# Install PostgreSQL
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" | tee /etc/apt/sources.list.d/pgdg.list && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    apt-get update && \
    apt-get -y install postgresql

# Install Gradle
ENV GRADLE_VERSION=8.1.1
ENV GRADLE_HOME /opt/gradle
ENV PATH="$PATH:$GRADLE_HOME/bin"

RUN curl -L -o /tmp/gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" && \
    unzip "/tmp/gradle.zip" -d /opt && \
    ln -s "/opt/gradle-${GRADLE_VERSION}" $GRADLE_HOME && \
    rm "/tmp/gradle.zip"

# Copy your Gradle scripts and source code into the image
COPY build.gradle settings.gradle gradlew /app/
COPY libs /app/libs
COPY src /app/src

# Execute the Gradle build
WORKDIR /app

RUN gradle wrapper clean build

# Run the JAR file when a container is started
CMD ["java", "-jar", "/app/build/libs/IDis.jar"]
