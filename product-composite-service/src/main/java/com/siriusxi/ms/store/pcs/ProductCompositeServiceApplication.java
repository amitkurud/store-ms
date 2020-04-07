package com.siriusxi.ms.store.pcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.siriusxi.ms.store")
public class ProductCompositeServiceApplication ***REMOVED***

    public static void main(String[] args) ***REMOVED***
        SpringApplication.run(ProductCompositeServiceApplication.class, args);
***REMOVED***

    @Bean
    RestTemplate newRestClient() ***REMOVED***
        return new RestTemplate();
***REMOVED***

***REMOVED***
