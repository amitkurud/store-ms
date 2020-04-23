#!/usr/bin/env bash
## author: Mohamed Taman
## version: v1.0
echo -e "\nInstalling all Springy store core shared modules"
echo -e  "***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"
echo "***REMOVED***"
./mvnw --quiet clean install -pl store-base/store-build-chassis || exit 126
echo -e "***REMOVED***\n"
echo "2- Installing shared [Services Utilities] module***REMOVED***"
./mvnw --quiet clean install -pl  store-common/store-utils || exit 126
echo -e "***REMOVED***\n"
echo "3- Installing shared [Services APIs] module***REMOVED***"
./mvnw --quiet clean install -pl  store-common/store-api || exit 126
echo -e "***REMOVED***\n"
echo "4- Installing [Services Parent Chassis] module***REMOVED***"
./mvnw --quiet clean install -pl store-base/store-service-chassis || exit 126
echo -e "***REMOVED***\n"

echo -e "***REMOVED***\n\
***REMOVED***"