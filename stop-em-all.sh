#!/usr/bin/env bash
## Author: Mohamed Taman
## version: v1.0

echo -e "Stopping [Springy Store] μServices ***REMOVED***.\n\
***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"
for port in 9080 9081 9082 9083
do
    echo "Stopping Microservice at port $port ***REMOVED***."
    curl -X POST localhost:$***REMOVED***port***REMOVED***/actuator/shutdown
    echo -e "\nMicroservice at port $***REMOVED***port***REMOVED*** stopped successfully ***REMOVED***. \n"
done
