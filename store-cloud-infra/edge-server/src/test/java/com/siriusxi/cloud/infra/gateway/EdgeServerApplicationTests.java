package com.siriusxi.cloud.infra.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"eureka.client.enabled: false"***REMOVED***)
class EdgeServerApplicationTests ***REMOVED***

  @Test
  void contextLoads() ***REMOVED***
    assert true;
***REMOVED***
***REMOVED***
