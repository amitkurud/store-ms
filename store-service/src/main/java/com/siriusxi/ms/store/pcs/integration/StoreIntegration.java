package com.siriusxi.ms.store.pcs.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.api.core.recommendation.RecommendationEndpoint;
import com.siriusxi.ms.store.api.core.review.Review;
import com.siriusxi.ms.store.api.core.review.ReviewService;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.HttpErrorInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.springframework.http.HttpMethod.GET;

@Component
@Log4j2
public class StoreIntegration
        implements
        ProductService,
        RecommendationEndpoint,
        ReviewService ***REMOVED***

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public StoreIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,

            @Value("$***REMOVED***app.product-service.host***REMOVED***") String productServiceHost,
            @Value("$***REMOVED***app.product-service.port***REMOVED***") int productServicePort,

            @Value("$***REMOVED***app.recommendation-service.host***REMOVED***") String recommendationServiceHost,
            @Value("$***REMOVED***app.recommendation-service.port***REMOVED***") int recommendationServicePort,

            @Value("$***REMOVED***app.review-service.host***REMOVED***") String reviewServiceHost,
            @Value("$***REMOVED***app.review-service.port***REMOVED***") int reviewServicePort
    ) ***REMOVED***

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        var http = "http://";

        productServiceUrl = http.concat(productServiceHost).concat(":").concat(valueOf(productServicePort))
                .concat("/products/");
        recommendationServiceUrl = http.concat(recommendationServiceHost).concat(":")
                .concat(valueOf(recommendationServicePort)).concat("/recommendations");
        reviewServiceUrl = http.concat(reviewServiceHost).concat(":").concat(valueOf(reviewServicePort))
                .concat("/reviews");
***REMOVED***

    @Override
    public Product createProduct(Product body) ***REMOVED***

        try ***REMOVED***
            String url = productServiceUrl;
            log.debug("Will post a new product to URL: ***REMOVED******REMOVED***", url);

            Product product = restTemplate.postForObject(url, body, Product.class);
            log.debug("Created a product with id: ***REMOVED******REMOVED***", product.getProductId());

            return product;

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public Product getProduct(int productId) ***REMOVED***

        try ***REMOVED***
            String url = productServiceUrl + "/" + productId;
            log.debug("Will call the getProduct API on URL: ***REMOVED******REMOVED***", url);

            Product product = restTemplate.getForObject(url, Product.class);
            log.debug("Found a product with id: ***REMOVED******REMOVED***", product.getProductId());

            return product;

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public void deleteProduct(int productId) ***REMOVED***
        try ***REMOVED***
            String url = productServiceUrl + "/" + productId;
            log.debug("Will call the deleteProduct API on URL: ***REMOVED******REMOVED***", url);

            restTemplate.delete(url);

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public Recommendation createRecommendation(Recommendation body) ***REMOVED***

        try ***REMOVED***
            String url = recommendationServiceUrl;
            log.debug("Will post a new recommendation to URL: ***REMOVED******REMOVED***", url);

            Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
            log.debug("Created a recommendation with id: ***REMOVED******REMOVED***", recommendation.getProductId());

            return recommendation;

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public List<Recommendation> getRecommendations(int productId) ***REMOVED***

        try ***REMOVED***
            String url = recommendationServiceUrl + "?productId=" + productId;

            log.debug("Will call the getRecommendations API on URL: ***REMOVED******REMOVED***", url);
            List<Recommendation> recommendations = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() ***REMOVED******REMOVED***).getBody();

            log.debug("Found ***REMOVED******REMOVED*** recommendations for a product with id: ***REMOVED******REMOVED***", recommendations.size(), productId);
            return recommendations;

    ***REMOVED*** catch (Exception ex) ***REMOVED***
            log.warn("Got an exception while requesting recommendations, return zero recommendations: ***REMOVED******REMOVED***", ex.getMessage());
            return new ArrayList<>();
    ***REMOVED***
***REMOVED***

    @Override
    public void deleteRecommendations(int productId) ***REMOVED***
        try ***REMOVED***
            String url = recommendationServiceUrl + "?productId=" + productId;
            log.debug("Will call the deleteRecommendations API on URL: ***REMOVED******REMOVED***", url);

            restTemplate.delete(url);

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public Review createReview(Review body) ***REMOVED***

        try ***REMOVED***
            String url = reviewServiceUrl;
            log.debug("Will post a new review to URL: ***REMOVED******REMOVED***", url);

            Review review = restTemplate.postForObject(url, body, Review.class);
            log.debug("Created a review with id: ***REMOVED******REMOVED***", review.getProductId());

            return review;

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    @Override
    public List<Review> getReviews(int productId) ***REMOVED***

        try ***REMOVED***
            String url = reviewServiceUrl + "?productId=" + productId;

            log.debug("Will call the getReviews API on URL: ***REMOVED******REMOVED***", url);
            List<Review> reviews = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() ***REMOVED******REMOVED***).getBody();

            log.debug("Found ***REMOVED******REMOVED*** reviews for a product with id: ***REMOVED******REMOVED***", reviews.size(), productId);
            return reviews;

    ***REMOVED*** catch (Exception ex) ***REMOVED***
            log.warn("Got an exception while requesting reviews, return zero reviews: ***REMOVED******REMOVED***", ex.getMessage());
            return new ArrayList<>();
    ***REMOVED***
***REMOVED***

    @Override
    public void deleteReviews(int productId) ***REMOVED***
        try ***REMOVED***
            String url = reviewServiceUrl + "?productId=" + productId;
            log.debug("Will call the deleteReviews API on URL: ***REMOVED******REMOVED***", url);

            restTemplate.delete(url);

    ***REMOVED*** catch (HttpClientErrorException ex) ***REMOVED***
            throw handleHttpClientException(ex);
    ***REMOVED***
***REMOVED***

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) ***REMOVED***
        switch (ex.getStatusCode()) ***REMOVED***
            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));
            case UNPROCESSABLE_ENTITY :
                return new InvalidInputException(getErrorMessage(ex));
            default:
                log.warn("Got a unexpected HTTP error: ***REMOVED******REMOVED***, will rethrow it", ex.getStatusCode());
                log.warn("Error body: ***REMOVED******REMOVED***", ex.getResponseBodyAsString());
                return ex;
    ***REMOVED***
***REMOVED***

    private String getErrorMessage(HttpClientErrorException ex) ***REMOVED***
        try ***REMOVED***
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).message();
    ***REMOVED*** catch (IOException ioException) ***REMOVED***
            return ex.getMessage();
    ***REMOVED***
***REMOVED***
***REMOVED***
