package com.siriusxi.cloud.infra.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EdgeServerApplication ***REMOVED***

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() ***REMOVED***
		return WebClient.builder();
	***REMOVED***

	public static void main(String[] args) ***REMOVED***
		SpringApplication.run(EdgeServerApplication.class, args);
	***REMOVED***

***REMOVED***
