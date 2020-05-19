package com.siriusxi.cloud.infra.cs;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"spring.profiles.active: native"***REMOVED***)
class CentralizedConfigServerTests ***REMOVED***

  @Test
  void contextLoads() ***REMOVED***
    Assert.assertTrue(true);
***REMOVED***
***REMOVED***
