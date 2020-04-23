#!/usr/bin/env bash
## Author: Mohamed Taman
## version: v4.1
### Sample usage:
#
#   for local run
#     ***REMOVED*** PORT=9080 ./test-em-all.bash
#   with docker compose
#     ***REMOVED*** PORT=8080 ./test-em-all.bash start stop
#
echo -e "***REMOVED***.\n"

: $***REMOVED******REMOVED******REMOVED***
: $***REMOVED***PORT=8080***REMOVED***
: $***REMOVED***PROD_ID_REVS_RECS=2***REMOVED***
: $***REMOVED***PROD_ID_NOT_FOUND=14***REMOVED***
: $***REMOVED***PROD_ID_NO_RECS=114***REMOVED***
: $***REMOVED***PROD_ID_NO_REVS=214***REMOVED***

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
    return 0
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      return 1
  fi
***REMOVED***

function assertEqual() ***REMOVED***

  local expected=$1
  local actual=$2

  if [[ "$actual" = "$expected" ]]
  then
    echo "Test OK (actual value: $actual)"
    return 0
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    return 1
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

function testCompositeCreated() ***REMOVED***

    # Expect that the Product Composite for productId $PROD_ID_REVS_RECS
    # has been created with three recommendations and three reviews
    if ! assertCurl 200 "curl http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$***REMOVED***PROD_ID_REVS_RECS***REMOVED*** -s"
    then
        echo -n "FAIL"
        return 1
    fi

    set +e
    assertEqual "$PROD_ID_REVS_RECS" $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
    if [[ "$?" -eq "1" ]] ; then return 1; fi

    assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
    if [[ "$?" -eq "1" ]] ; then return 1; fi

    assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")
    if [[ "$?" -eq "1" ]] ; then return 1; fi

    set -e
***REMOVED***

function waitForMessageProcessing() ***REMOVED***
    echo "Wait for messages to be processed***REMOVED*** "

    # Give background processing some time to complete***REMOVED***
    sleep 1

    n=0
    until testCompositeCreated
    do
        n=$((n + 1))
        if [[ $***REMOVED***n***REMOVED*** == 40 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
    echo "All messages are now processed!"
***REMOVED***

function recreateComposite() ***REMOVED***
    local productId=$1
    local composite=$2

    assertCurl 200 "curl -X DELETE http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$***REMOVED***productId***REMOVED*** -s"
    curl -X POST http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED*** -H "Content-Type: application/json" --data "$composite"
***REMOVED***

function setupTestData() ***REMOVED***

    body="***REMOVED***\"productId\":$PROD_ID_NO_RECS"
    body+=\
',"name":"product name A","weight":100, "reviews":[
    ***REMOVED***"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"***REMOVED***,
    ***REMOVED***"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"***REMOVED***,
    ***REMOVED***"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"***REMOVED***
]***REMOVED***'
    recreateComposite "$PROD_ID_NO_RECS" "$body"

    body="***REMOVED***\"productId\":$PROD_ID_NO_REVS"
    body+=\
',"name":"product name B","weight":200, "recommendations":[
    ***REMOVED***"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"***REMOVED***,
    ***REMOVED***"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"***REMOVED***,
    ***REMOVED***"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"***REMOVED***
]***REMOVED***'
    recreateComposite "$PROD_ID_NO_REVS" "$body"


    body="***REMOVED***\"productId\":$PROD_ID_REVS_RECS"
    body+=\
',"name":"product name C","weight":300, "recommendations":[
        ***REMOVED***"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"***REMOVED***,
        ***REMOVED***"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"***REMOVED***,
        ***REMOVED***"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"***REMOVED***
    ], "reviews":[
        ***REMOVED***"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"***REMOVED***,
        ***REMOVED***"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"***REMOVED***,
        ***REMOVED***"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"***REMOVED***
    ]***REMOVED***'

    recreateComposite 1 "$body"
***REMOVED***

set -e

echo "Start Tests:" `date`

echo "HOST=$***REMOVED***HOST***REMOVED***"
echo "PORT=$***REMOVED***PORT***REMOVED***"

if [[ $@ == *"start"* ]]
then
    echo "***REMOVED***"
    echo "***REMOVED***"
    docker-compose -p ssm down --remove-orphans
    echo "***REMOVED***"
    docker-compose -p ssm up -d
fi

waitForService curl http://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***/actuator/health

setupTestData

waitForMessageProcessing

# Verify that a normal request works, expect three recommendations and three reviews
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS -s"
assertEqual $***REMOVED***PROD_ID_REVS_RECS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 404 (Not Found) error is returned for a non existing productId (13)
assertCurl 404 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NOT_FOUND -s"

# Verify that no recommendations are returned for productId 113
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NO_RECS -s"
assertEqual $***REMOVED***PROD_ID_NO_RECS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that no reviews are returned for productId 213
assertCurl 200 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NO_REVS -s"
assertEqual $***REMOVED***PROD_ID_NO_REVS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 422 (Unprocessable Entity) error is returned for a productId that is out of range (-1)
assertCurl 422 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/-1 -s"
assertEqual "\"Invalid productId: -1\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a productId that is not a number, i.e. invalid format
assertCurl 400 "curl http://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/invalidProductId -s"
assertEqual "\"Type mismatch.\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

echo "End, all tests OK:" `date`

if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment***REMOVED***"
     echo "$ docker-compose down --remove-orphans"
    docker-compose -p ssm down --remove-orphans
fi