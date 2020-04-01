#!/usr/bin/env bash
## Author: Mohamed Taman
## version: v1.0

echo -e "Starting [Springy Store] μServices ***REMOVED***.\n\
***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"

function runService()***REMOVED***
   cd "$1" && \
   mvn --quiet spring-boot:run -Dspring-boot.run.jvmArguments="--enable-preview"
***REMOVED***

for dir in `find *-service -maxdepth 0 -type d`
do
    echo -e "Starting [$dir] Microservice***REMOVED***. \n" && \
    runService "$dir" &
done