#!/usr/bin/env bash
## Author: Mohamed Taman
## version: v3.0
### Sample usage:
#
#   for local run
#     ***REMOVED*** PORT=9080 ./test-em-all.bash
#   with docker compose
#     ***REMOVED*** PORT=8080 ./test-em-all.bash start stop
#
echo -e "Starting [Springy Store] full functionality testing***REMOVED***.\n"

: $***REMOVED******REMOVED******REMOVED***
: $***REMOVED***PORT=8080***REMOVED***

BASE_URL="/store/api/v1/products"
function assertCurl() ***REMOVED***

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%***REMOVED***http_code***REMOVED***\""
  local result=$(eval $***REMOVED***curlCmd***REMOVED***)
  local httpCode="$***REMOVED***result:(-3)***REMOVED***"
  RESPONSE='' && (( $***REMOVED***#result***REMOVED*** > 3 )) && RESPONSE="$***REMOVED***result%???***REMOVED***"

  if [[ "$httpCode" = "$expectedHttpCode" ]]
  then
    if [[ "$httpCode" = "200" ]]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
***REMOVED***

function assertEqual() ***REMOVED***

  local expected=$1
  local actual=$2

  if [[ "$actual" = "$expected" ]]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
***REMOVED***

function testUrl() ***REMOVED***
    url=$@
    if curl $***REMOVED***url***REMOVED*** -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
***REMOVED***

function waitForService() ***REMOVED***
    url=$@
    echo -n "Wait for: $url***REMOVED*** "
    n=0
    until testUrl $***REMOVED***url***REMOVED***
    do
        n=$((n + 1))
        if [[ $***REMOVED***n***REMOVED*** == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
***REMOVED***

function createProduct() ***REMOVED***
    local productId=$1
    local composite=$2

    assertCurl 200 "curl -X DELETE http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$***REMOVED***productId***REMOVED*** -s"
    curl -X POST http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED*** -H "Content-Type: application/json" --data "$composite"
***REMOVED***

function setupTestData() ***REMOVED***

    body=\
'***REMOVED***"productId":1,"name":"product 1","weight":1, "recommendations":[
        ***REMOVED***"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"***REMOVED***,
        ***REMOVED***"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"***REMOVED***,
        ***REMOVED***"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"***REMOVED***
    ], "reviews":[
        ***REMOVED***"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"***REMOVED***,
        ***REMOVED***"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"***REMOVED***,
        ***REMOVED***"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"***REMOVED***
    ]***REMOVED***'
    createProduct 1 "$body"

    body=\
'***REMOVED***"productId":113,"name":"product 113","weight":113, "reviews":[
    ***REMOVED***"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"***REMOVED***,
    ***REMOVED***"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"***REMOVED***,
    ***REMOVED***"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"***REMOVED***
]***REMOVED***'
    createProduct 113 "$body"

    body=\
'***REMOVED***"productId":213,"name":"product 213","weight":213, "recommendations":[
    ***REMOVED***"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"***REMOVED***,
    ***REMOVED***"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"***REMOVED***,
    ***REMOVED***"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"***REMOVED***
]***REMOVED***'
    createProduct 213 "$body"
***REMOVED***

set -e

echo "Start:" `date`

echo "HOST=$***REMOVED***HOST***REMOVED***"
echo "PORT=$***REMOVED***PORT***REMOVED***"

if [[ $@ == *"start"* ]]
then
    echo "***REMOVED***"
    echo "$ docker-compose down"
    docker-compose down
    echo "***REMOVED***"
    docker-compose -p ssm up -d
fi

waitForService curl -X DELETE http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/13

setupTestData

# Verify that a normal request works, expect three recommendations and three reviews
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/1 -s"
assertEqual 1 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 404 (Not Found) error is returned for a non existing productId (13)
assertCurl 404 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/13 -s"

# Verify that no recommendations are returned for productId 113
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/113 -s"
assertEqual 113 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that no reviews are returned for productId 213
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/213 -s"
assertEqual 213 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 422 (Unprocessable Entity) error is returned for a productId that is out of range (-1)
assertCurl 422 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/-1 -s"
assertEqual "\"Invalid productId: -1\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a productId that is not a number, i.e. invalid format
assertCurl 400 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/invalidProductId -s"
assertEqual "\"Type mismatch.\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment***REMOVED***"
    echo "$ docker-compose down"
    docker-compose -p ssm down
fi

echo "End:" `date`