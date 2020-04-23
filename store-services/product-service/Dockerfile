#### Start of builder image
# ***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***
# Builder stage to prepare application for final image
FROM openjdk:14-slim-buster as builder
WORKDIR temp

# Fatjar location, but could be set to different location from command line
ARG JAR_FILE=target/*.jar

# Copy fat jar file to current image builder
COPY $***REMOVED***JAR_FILE***REMOVED*** application.jar

# Extract the jar file layers
RUN java -Djarmode=layertools -jar --enable-preview application.jar extract
#### End of builder stage

#### Start of actual image
# ***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***
# Build image based on JDK 14 base image, based on latest debian buster OS
FROM openjdk:14-slim-buster
VOLUME /tmp

# Set image information, but could be set to different location from command line
ARG IMAGE_VERSION="0.0.1-SNAPSHOT"
ARG IMAGE_NAME="Product Service"
ARG MAINTAINER="Mohamed Taman <mohamed.taman@gmail.com>"

LABEL version=$***REMOVED***IMAGE_VERSION***REMOVED*** name=$***REMOVED***IMAGE_NAME***REMOVED*** maintainer=$***REMOVED***MAINTAINER***REMOVED***

# Limiting security access to not user root user
RUN addgroup siriusxi && useradd -g siriusxi -ms /bin/bash taman

# Setting user to current created user
USER taman

# Set working directory to application folder
WORKDIR /home/taman/application

# Copy all layers from builder stage to current image
COPY --from=builder temp/dependencies/ ./
COPY --from=builder temp/snapshot-dependencies/ ./
COPY --from=builder temp/spring-boot-loader/ ./
COPY --from=builder temp/application/ ./

# Expose current application to port 8080
EXPOSE 8080

ARG JAVA_OPTS=""

# Run the application with JVM configs if any
ENTRYPOINT ["bash", "-c", \
"java -server --enable-preview -XX:+UseContainerSupport \
-XX:+AlwaysActAsServerClassMachine -XX:+UseG1GC -XX:+UseStringDeduplication $***REMOVED***JAVA_OPTS***REMOVED*** \
org.springframework.boot.loader.JarLauncher $***REMOVED***0***REMOVED*** $***REMOVED***@***REMOVED***"]