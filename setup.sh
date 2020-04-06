#!/usr/bin/env bash
## author: Mohamed Taman
## version: v1.0
echo -e "\nInstalling all Springy store core shared modules"
echo -e  "***REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED******REMOVED***\n"
echo "1- Installing shared [Utilities] module***REMOVED***"
cd store-utils && mvn --quiet clean install || exit 126
echo -e "***REMOVED***\n"
echo "2- Installing shared [APIs] module***REMOVED***"
cd ../store-api && mvn --quiet clean install || exit 126
echo -e "***REMOVED***\n"
echo "3- Installing [parent project] module***REMOVED***"
cd ../store-chassis && mvn -N --quiet clean install
echo -e "***REMOVED***\n"
echo -e "***REMOVED***\n\
***REMOVED***"