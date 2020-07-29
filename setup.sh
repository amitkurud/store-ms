#!/usr/bin/env bash
## author: Mohamed Taman
## version: v4.5
echo -e "\n***REMOVED***"
echo -e  "***REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -U -pl store-base/store-build-chassis || exit 126
echo -e "***REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -U -pl store-base/store-cloud-chassis || exit 126
echo -e "***REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -pl store-common/store-utils || exit 126
echo -e "***REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -pl store-common/store-api || exit 126
echo -e "***REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -U -pl store-base/store-service-chassis || exit 126
echo -e "***REMOVED***\n"

echo -e "***REMOVED***\n\
***REMOVED***"