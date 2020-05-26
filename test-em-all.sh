#!/usr/bin/env bash
## Author: Mohamed Taman
## version: v5.0
### Sample usage:
#
#   for local run
#     ***REMOVED*** ***REMOVED*** ./test-em-all.bash
#   with docker compose
#     ***REMOVED*** ***REMOVED*** ./test-em-all.bash start stop
#
echo -e "Starting 'Springy Store μServices' for [end-2-end] testing***REMOVED***.\n"

: $***REMOVED******REMOVED******REMOVED***
: $***REMOVED******REMOVED******REMOVED***
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
          return 0
    else
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
            sleep 3
            echo -n ", retry #$n "
        fi
    done
    echo -e "\n DONE, continues***REMOVED***\n"
***REMOVED***

function testCompositeCreated() ***REMOVED***

    # Expect that the Product Composite for productId $PROD_ID_REVS_RECS
    # has been created with three recommendations and three reviews
    if ! assertCurl 200 "curl $AUTH -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$***REMOVED***PROD_ID_REVS_RECS***REMOVED*** -s"
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

    assertCurl 200 "curl $AUTH -X DELETE -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$***REMOVED***productId***REMOVED*** -s"
    curl -X POST -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED*** -H "Content-Type: application/json" -H \
    "Authorization: Bearer $ACCESS_TOKEN" \
    --data "$composite"
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

function testCircuitBreaker() ***REMOVED***

    echo "***REMOVED***"

    EXEC="docker run --rm -it --network=ssm_default alpine"

    # First, use the health - endpoint to verify that the circuit breaker is closed
    assertEqual "CLOSED" "$($***REMOVED***EXEC***REMOVED*** wget store:8080/actuator/health -qO - | \
     jq -r .components.productCircuitBreaker.details.state)"

    # Open the circuit breaker by running three slow calls in a row,
    # i.e. that cause a timeout exception
    # Also, verify that we get 500 back and a timeout related error message
    for ((n=0; n<3; n++))
    do
        assertCurl 500 "curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS?delay=3 $AUTH -s"
        message=$(echo $***REMOVED***RESPONSE***REMOVED*** | jq -r .message)
        assertEqual "Did not observe any item or terminal signal within 2000ms" "$***REMOVED***message:0:57***REMOVED***"
    done

    # Verify that the circuit breaker now is open by running the slow call again, verify it gets 200 back, i.e. fail fast works, and a response from the fallback method.
    assertCurl 200 "curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS?delay=3 $AUTH -s"
    assertEqual "Fallback product2" "$(echo "$RESPONSE" | jq -r .name)"

    # Also, verify that the circuit breaker is open by running a normal call, verify it also gets 200 back and a response from the fallback method.
    assertCurl 200 "curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS $AUTH -s"
    assertEqual "Fallback product2" "$(echo "$RESPONSE" | jq -r .name)"

    # Verify that a 404 (Not Found) error is returned for a non existing productId ($PROD_ID_NOT_FOUND) from the fallback method.
    assertCurl 404 "curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NOT_FOUND $AUTH -s"
    assertEqual "Product Id: $PROD_ID_NOT_FOUND not found in fallback cache!" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq -r .message)"

    # Wait for the circuit breaker to transition to the half open state (i.e. max 10 sec)
    echo "Will sleep for 10 sec waiting for the CB to go Half Open***REMOVED***"
    sleep 10

    # Verify that the circuit breaker is in half open state
    assertEqual "HALF_OPEN" "$($***REMOVED***EXEC***REMOVED*** wget store:8080/actuator/health -qO - | \
     jq -r .components.productCircuitBreaker.details.state)"

    # Close the circuit breaker by running three normal calls in a row
    # Also, verify that we get 200 back and a response based on information in the product database
    for ((n=0; n<3; n++))
    do
        assertCurl 200 "curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS $AUTH -s"
        assertEqual "product name C" "$(echo "$RESPONSE" | jq -r .name)"
    done

    # Verify that the circuit breaker is in closed state again
    assertEqual "CLOSED" "$($***REMOVED***EXEC***REMOVED*** wget store:8080/actuator/health -qO - | \
    jq -r .components.productCircuitBreaker.details.state)"

    # Verify that the expected state transitions happened in the circuit breaker
    assertEqual "CLOSED_TO_OPEN"      "$($***REMOVED***EXEC***REMOVED*** wget \
    store:8080/actuator/circuitbreakerevents/product/STATE_TRANSITION -qO - | \
    jq -r .circuitBreakerEvents[-3].stateTransition)"
    assertEqual "OPEN_TO_HALF_OPEN"   "$($***REMOVED***EXEC***REMOVED*** wget \
    store:8080/actuator/circuitbreakerevents/product/STATE_TRANSITION -qO - | \
    jq -r .circuitBreakerEvents[-2].stateTransition)"
    assertEqual "HALF_OPEN_TO_CLOSED" "$($***REMOVED***EXEC***REMOVED*** wget \
    store:8080/actuator/circuitbreakerevents/product/STATE_TRANSITION -qO - | \
    jq -r .circuitBreakerEvents[-1].stateTransition)"
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

waitForService curl -k https://$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***/actuator/health

ACCESS_TOKEN=$(curl -k https://writer:secret@$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***/oauth/token \
                    -d grant_type=password -d username=taman -d password=password -s \
                     | jq .access_token -r)

AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""

setupTestData

waitForMessageProcessing

# Verify that a normal request works, expect three recommendations and three reviews
assertCurl 200 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS $AUTH -s"
assertEqual $***REMOVED***PROD_ID_REVS_RECS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 404 (Not Found) error is returned for a non existing productId (13)
assertCurl 404 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NOT_FOUND $AUTH -s"

# Verify that no recommendations are returned for productId 113
assertCurl 200 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NO_RECS $AUTH -s"
assertEqual $***REMOVED***PROD_ID_NO_RECS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that no reviews are returned for productId 213
assertCurl 200 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_NO_REVS $AUTH -s"
assertEqual $***REMOVED***PROD_ID_NO_REVS***REMOVED*** $(echo $***REMOVED***RESPONSE***REMOVED*** | jq .productId)
assertEqual 3 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".recommendations | length")
assertEqual 0 $(echo $***REMOVED***RESPONSE***REMOVED*** | jq ".reviews | length")

# Verify that a 422 (Unprocessable Entity) error is returned for a productId that is out of range (-1)
assertCurl 422 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/-1 $AUTH -s"
assertEqual "\"Invalid productId: -1\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a productId that is not a number, i.e. invalid format
assertCurl 400 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/invalidProductId $AUTH -s"
assertEqual "\"Type mismatch.\"" "$(echo $***REMOVED***RESPONSE***REMOVED*** | jq .message)"

# Verify that a request without access token fails on 401, Unauthorized
assertCurl 401 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS -s"

# Verify that the reader - client with only read scope can call the read API but not delete API.
READER_ACCESS_TOKEN=$(curl -k https://reader:secret@$***REMOVED***HOST***REMOVED***:$***REMOVED***PORT***REMOVED***/oauth/token \
                           -d grant_type=password -d username=taman -d password=password -s | \
                            jq .access_token -r)

READER_AUTH="-H \"Authorization: Bearer $READER_ACCESS_TOKEN\""

assertCurl 200 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS $READER_AUTH -s"
assertCurl 403 "curl -k https://$HOST:$PORT$***REMOVED***BASE_URL***REMOVED***/$PROD_ID_REVS_RECS $READER_AUTH -X DELETE -s"

testCircuitBreaker

echo "End, all tests OK:" `date`

if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment***REMOVED***"
    echo "***REMOVED***"
    docker-compose -p ssm down --remove-orphans
fi