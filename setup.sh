#!/usr/bin/env bash
## author: Mohamed Taman
## version: v1.0
echo -e "\nInstalling all Springy store core shared modules"
echo -e  "***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"
echo "1- Installing shared [Utilities] module***REMOVED***"
./mvnw --quiet clean install -f store-utils || exit 126
echo -e "***REMOVED***\n"
echo "2- Installing shared [APIs] module***REMOVED***"
./mvnw --quiet clean install -f store-api || exit 126
echo -e "***REMOVED***\n"
echo "3- Installing [Parent] module***REMOVED***"
./mvnw --quiet clean install -N -f store-chassis || exit 126
echo -e "***REMOVED***\n"
echo -e "***REMOVED***\n\
***REMOVED***"