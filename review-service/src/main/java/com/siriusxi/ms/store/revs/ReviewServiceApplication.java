package com.siriusxi.ms.store.revs;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan("com.siriusxi.ms.store")
@Log4j2
public class ReviewServiceApplication ***REMOVED***

  public static void main(String[] args) ***REMOVED***

    var mysqlUri = run(ReviewServiceApplication.class, args)
            .getEnvironment()
            .getProperty("spring.datasource.url");

    // Useful logged info about which database it is connected to.
    log.info("Connected to MySQL: ***REMOVED******REMOVED***", mysqlUri);
***REMOVED***

  @Bean
  public CommandLineRunner runner() ***REMOVED***
    return r -> log.info("Review Microservice started successfully.");
***REMOVED***
***REMOVED***
