package com.siriusxi.ms.store.pcs.config;

import com.siriusxi.ms.store.pcs.integration.StoreIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Map;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class StoreServiceConfiguration ***REMOVED***

  private final StoreIntegration integration;

  @Value("$***REMOVED***api.common.version***REMOVED***")
  private String apiVersion;

  @Value("$***REMOVED***api.common.title***REMOVED***")
  private String apiTitle;

  @Value("$***REMOVED***api.common.description***REMOVED***")
  private String apiDescription;

  @Value("$***REMOVED***api.common.termsOfServiceUrl***REMOVED***")
  private String apiTermsOfServiceUrl;

  @Value("$***REMOVED***api.common.license***REMOVED***")
  private String apiLicense;

  @Value("$***REMOVED***api.common.licenseUrl***REMOVED***")
  private String apiLicenseUrl;

  @Value("$***REMOVED***api.common.contact.name***REMOVED***")
  private String apiContactName;

  @Value("$***REMOVED***api.common.contact.url***REMOVED***")
  private String apiContactUrl;

  @Value("$***REMOVED***api.common.contact.email***REMOVED***")
  private String apiContactEmail;

  @Autowired
  public StoreServiceConfiguration(StoreIntegration integration) ***REMOVED***
    this.integration = integration;
***REMOVED***

  @Bean(name = "Core System Microservices")
  ReactiveHealthContributor coreServices() ***REMOVED***

    ReactiveHealthIndicator productHealthIndicator = integration::getProductHealth;
    ReactiveHealthIndicator recommendationHealthIndicator = integration::getRecommendationHealth;
    ReactiveHealthIndicator reviewHealthIndicator = integration::getReviewHealth;

    Map<String, ReactiveHealthContributor> allIndicators =
        Map.of(
            "Product Service", productHealthIndicator,
            "Recommendation Service", recommendationHealthIndicator,
            "Review Service", reviewHealthIndicator);

    return CompositeReactiveHealthContributor.fromMap(allIndicators);
***REMOVED***

  @Bean
  RestTemplate newRestClient() ***REMOVED***
    return new RestTemplate();
***REMOVED***

  /**
   * Will exposed on $HOST:$PORT/swagger-ui.html
   *
   * @return Docket swagger configuration
   */
  @Bean
  public Docket apiDocumentation() ***REMOVED***

    return new Docket(SWAGGER_2)
        .select()
        /*
           Using the apis() and paths() methods,
           we can specify where SpringFox shall look for API documentation.
        */
        .apis(basePackage("com.siriusxi.ms.store"))
        .paths(PathSelectors.any())
        .build()
        /*
            Using the globalResponseMessage() method, we ask SpringFox not to add any default HTTP
            response codes to the API documentation, such as 401 and 403,
            which we don't currently use.
        */
        .globalResponseMessage(POST, emptyList())
        .globalResponseMessage(GET, emptyList())
        .globalResponseMessage(DELETE, emptyList())
        /*
            The api* variables that are used to configure the Docket bean with general
            information about the API are initialized from the property file using
            Spring @Value annotations.
        */
        .apiInfo(
            new ApiInfo(
                apiTitle,
                apiDescription,
                apiVersion,
                apiTermsOfServiceUrl,
                new Contact(apiContactName, apiContactUrl, apiContactEmail),
                apiLicense,
                apiLicenseUrl,
                emptyList()));
***REMOVED***
***REMOVED***
