package com.siriusxi.cloud.infra.eds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class EurekaDiscoveryServerTests ***REMOVED***

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void catalogLoads() ***REMOVED***

		String expectedReponseBody = "***REMOVED***\"applications\":***REMOVED***\"versions__delta\":\"1\",\"apps__hashcode\":\"\",\"application\":[]***REMOVED******REMOVED***";
		ResponseEntity<String> entity = testRestTemplate.getForEntity("/eureka/apps", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(expectedReponseBody, entity.getBody());
	***REMOVED***

	@Test
	public void healthy() ***REMOVED***
		String expectedReponseBody = "***REMOVED***\"status\":\"UP\"***REMOVED***";
		ResponseEntity<String> entity = testRestTemplate.getForEntity("/actuator/health", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(expectedReponseBody, entity.getBody());
	***REMOVED***

***REMOVED***
