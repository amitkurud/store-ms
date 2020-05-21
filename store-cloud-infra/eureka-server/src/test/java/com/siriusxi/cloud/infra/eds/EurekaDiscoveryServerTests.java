package com.siriusxi.cloud.infra.eds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***
      "spring.cloud.config.enabled: false",
      "eureka.client.register-with-eureka: false",
      "eureka.client.fetch-registry: false",
      "app.eureka.user: mt",
      "app.eureka.pass: p"
***REMOVED***)
class EurekaDiscoveryServerTests ***REMOVED***

  @Value("$***REMOVED***app.eureka.user***REMOVED***")
  private String username;

  @Value("$***REMOVED***app.eureka.pass***REMOVED***")
  private String password;

  private TestRestTemplate testRestTemplate;

  @Autowired
  public void setTestRestTemplate(TestRestTemplate testRestTemplate) ***REMOVED***
    this.testRestTemplate = testRestTemplate.withBasicAuth(username, password);
***REMOVED***

  @Test
  public void catalogLoads() ***REMOVED***

    String expectedResponseBody =
        "***REMOVED***\"applications\":***REMOVED***\"versions__delta\":\"1\","
            + "\"apps__hashcode\":\"\",\"application\":[]***REMOVED******REMOVED***";
    ResponseEntity<String> entity = testRestTemplate.getForEntity("/eureka/apps", String.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals(expectedResponseBody, entity.getBody());
***REMOVED***

  @Test
  public void healthy() ***REMOVED***
    String expectedResponseBody = "***REMOVED***\"status\":\"UP\"***REMOVED***";
    ResponseEntity<String> entity = testRestTemplate.getForEntity("/actuator/health", String.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals(expectedResponseBody, entity.getBody());
***REMOVED***
***REMOVED***
