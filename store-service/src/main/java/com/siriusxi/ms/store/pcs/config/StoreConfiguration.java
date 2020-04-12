package com.siriusxi.ms.store.pcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class StoreConfiguration ***REMOVED***
    @Value("$***REMOVED***api.common.version***REMOVED***")
    String apiVersion;
    @Value("$***REMOVED***api.common.title***REMOVED***")
    String apiTitle;
    @Value("$***REMOVED***api.common.description***REMOVED***")
    String apiDescription;
    @Value("$***REMOVED***api.common.termsOfServiceUrl***REMOVED***")
    String apiTermsOfServiceUrl;
    @Value("$***REMOVED***api.common.license***REMOVED***")
    String apiLicense;
    @Value("$***REMOVED***api.common.licenseUrl***REMOVED***")
    String apiLicenseUrl;
    @Value("$***REMOVED***api.common.contact.name***REMOVED***")
    String apiContactName;
    @Value("$***REMOVED***api.common.contact.url***REMOVED***")
    String apiContactUrl;
    @Value("$***REMOVED***api.common.contact.email***REMOVED***")
    String apiContactEmail;

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
                    Using the globalResponseMessage() method, we ask SpringFox not to add any default HTTP response codes to the API documentation, such as 401 and 403, which we don't currently use.
                 */
                .globalResponseMessage(POST, emptyList())
                .globalResponseMessage(GET, emptyList())
                .globalResponseMessage(DELETE, emptyList())
                /*
                    The api* variables that are used to configure the Docket bean with general information about the API are initialized from the property file using Spring @Value annotations.
                 */
                .apiInfo(new ApiInfo(
                        apiTitle,
                        apiDescription,
                        apiVersion,
                        apiTermsOfServiceUrl,
                        new Contact(apiContactName, apiContactUrl, apiContactEmail),
                        apiLicense,
                        apiLicenseUrl,
                        emptyList()
                ));
***REMOVED***
***REMOVED***