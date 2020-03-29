package com.siriusxi.ms.store.pcs.services;

import com.siriusxi.ms.store.api.core.product.Product;
import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.recommendation.Recommendation;
import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.api.core.review.Review;
import com.siriusxi.ms.store.api.core.review.ReviewService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.HttpErrorInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.*;
import static org.springframework.http.HttpMethod.GET;

@Log4j2
@Component
public class ProductCompositeIntegration
        implements
        ProductService,
        RecommendationService,
        ReviewService ***REMOVED***

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,

            @Value("$***REMOVED***app.product-service.host***REMOVED***") String productServiceHost,
            @Value("$***REMOVED***app.product-service.port***REMOVED***") int    productServicePort,

            @Value("$***REMOVED***app.recommendation-service.host***REMOVED***") String recommendationServiceHost,
            @Value("$***REMOVED***app.recommendation-service.port***REMOVED***") int    recommendationServicePort,

            @Value("$***REMOVED***app.review-service.host***REMOVED***") String reviewServiceHost,
            @Value("$***REMOVED***app.review-service.port***REMOVED***") int    reviewServicePort
    ) ***REMOVED***

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        var http = "http://";
        productServiceUrl        = http.concat(productServiceHost).concat(":").concat(valueOf(productServicePort))
                .concat("/product/");
        recommendationServiceUrl = http.concat(recommendationServiceHost).concat(":")
                .concat(valueOf(recommendationServicePort)).concat("/recommendation?productId=");
        reviewServiceUrl         = http.concat(reviewServiceHost).concat(":").concat(valueOf(reviewServicePort))
                .concat("/review?productId=");
***REMOVED***

    @Override
    public Product getProduct(int productId) ***REMOVED***

        try ***REMOVED***
            String url = productServiceUrl + productId;
            log.debug("Will call getProduct API on URL: ***REMOVED******REMOVED***", url);

            Product product = restTemplate.getForObject(url, Product.class);
            log.debug("Found a product with id: ***REMOVED******REMOVED***", product != null ? product.getProductId() : "No Product found!!");

            return product;

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***

            switch (ex.getStatusCode()) ***REMOVED***
                case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
                case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));
                default -> ***REMOVED***
                    log.warn("Got a unexpected HTTP error: ***REMOVED******REMOVED***, will rethrow it", ex.getStatusCode());
                    log.warn("Error body: ***REMOVED******REMOVED***", ex.getResponseBodyAsString());
                    throw ex;
            ***REMOVED***
        ***REMOVED***
    ***REMOVED***
***REMOVED***

    @Override
    public List<Recommendation> getRecommendations(int productId) ***REMOVED***

        try ***REMOVED***
            String url = recommendationServiceUrl + productId;

            log.debug("Will call getRecommendations API on URL: ***REMOVED******REMOVED***", url);
            List<Recommendation> recommendations = restTemplate
                    .exchange(url, GET, null,
                            new ParameterizedTypeReference<List<Recommendation>>() ***REMOVED******REMOVED***)
                    .getBody();

            log.debug("Found ***REMOVED******REMOVED*** recommendations for a product with id: ***REMOVED******REMOVED***",
                    recommendations != null ? recommendations.size() : "***REMOVED***No Recommendations***REMOVED***",
                    productId);

            return recommendations;

    ***REMOVED*** catch (Exception ex) ***REMOVED***
            log.warn("Got an exception while requesting recommendations, return zero recommendations: ***REMOVED******REMOVED***",
                    ex.getMessage());

            return new ArrayList<>();
    ***REMOVED***
***REMOVED***

    @Override
    public List<Review> getReviews(int productId) ***REMOVED***

        try ***REMOVED***
            String url = reviewServiceUrl + productId;

            log.debug("Will call getReviews API on URL: ***REMOVED******REMOVED***", url);
            List<Review> reviews = restTemplate.exchange(
                    url,
                    GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() ***REMOVED******REMOVED***)
                    .getBody();

            log.debug("Found ***REMOVED******REMOVED*** reviews for a product with id: ***REMOVED******REMOVED***",
                    reviews != null ? reviews.size() : "***REMOVED***No Reviews***REMOVED***",
                    productId);

            return reviews;

    ***REMOVED*** catch (Exception ex) ***REMOVED***
            log.warn("Got an exception while requesting reviews, return zero reviews: ***REMOVED******REMOVED***", ex.getMessage());
            return new ArrayList<>();
    ***REMOVED***
***REMOVED***

    private String getErrorMessage(HttpClientErrorException ex) ***REMOVED***
        try ***REMOVED***
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).message();
    ***REMOVED*** catch (IOException ioex) ***REMOVED***
            return ex.getMessage();
    ***REMOVED***
***REMOVED***
***REMOVED***
