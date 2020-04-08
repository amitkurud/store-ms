#!/usr/bin/env bash
## author: Mohamed Taman
## version: v1.0
echo -e "\nInstalling all Springy store core shared modules"
echo -e  "***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"
echo "1- Installing [build parent] module***REMOVED***"
./mvnw --quiet clean install -pl store-build-chassis || exit 126
echo -e "***REMOVED***\n"
echo "2- Installing shared [Utilities] module***REMOVED***"
./mvnw --quiet clean install -pl store-utils || exit 126
echo -e "***REMOVED***\n"
echo "3- Installing shared [APIs] module***REMOVED***"
./mvnw --quiet clean install -pl store-api || exit 126
echo -e "***REMOVED***\n"
echo "4- Installing [service parent] module***REMOVED***"
./mvnw --quiet clean install -pl store-service-chassis || exit 126
echo -e "***REMOVED***\n"

echo -e "***REMOVED***\n\
***REMOVED***"