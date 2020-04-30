package com.siriusxi.cloud.infra.gateway.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@Log4j2
public class GatewayConfiguration ***REMOVED***

  private final WebClient.Builder webClientBuilder;

  private WebClient webClient;

  @Autowired
  public GatewayConfiguration(WebClient.Builder webClientBuilder) ***REMOVED***
    this.webClientBuilder = webClientBuilder;
***REMOVED***

    /**
     * This method is to check all the services health status, and Gateway service health will be
     * only in up health state if and only if all of the core services and dependencies are up
     * and running.
     *
     * @return ReactiveHealthContributor information about all the core microservices.
     */
    @Bean(name = "Core Microservices")
    ReactiveHealthContributor coreServices() ***REMOVED***

        ReactiveHealthIndicator productHealthIndicator = () -> getServicesHealth("http://product");
        ReactiveHealthIndicator recommendationHealthIndicator = () -> getServicesHealth("http://recommendation");
        ReactiveHealthIndicator reviewHealthIndicator = () -> getServicesHealth("http://review");
        ReactiveHealthIndicator storeHealthIndicator = () -> getServicesHealth("http://store");

        Map<String, ReactiveHealthContributor> allIndicators =
                Map.of(
                        "Product Service", productHealthIndicator,
                        "Recommendation Service", recommendationHealthIndicator,
                        "Review Service", reviewHealthIndicator,
                        "Store Service",storeHealthIndicator);

        return CompositeReactiveHealthContributor.fromMap(allIndicators);
***REMOVED***

  private Mono<Health> getServicesHealth(String url) ***REMOVED***
    url += "/actuator/health";
    log.debug("Will call the Health API on URL: ***REMOVED******REMOVED***", url);
    return getWebClient()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .map(s -> new Health.Builder().up().build())
        .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
        .log();
***REMOVED***

  private WebClient getWebClient() ***REMOVED***
    if (webClient == null) ***REMOVED***
      webClient = webClientBuilder.build();
***REMOVED***
    return webClient;
***REMOVED***
***REMOVED***