package com.siriusxi.ms.store.pcs.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.api.core.review.ReviewService;
import com.siriusxi.ms.store.api.core.review.dto.Review;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.HttpErrorInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

import static com.siriusxi.ms.store.api.event.Event.Type.CREATE;
import static com.siriusxi.ms.store.api.event.Event.Type.DELETE;
import static com.siriusxi.ms.store.pcs.integration.StoreIntegration.MessageSources;
import static java.lang.String.valueOf;
import static org.springframework.integration.support.MessageBuilder.withPayload;
import static reactor.core.publisher.Flux.empty;

@EnableBinding(MessageSources.class)
@Component
@Log4j2
public class StoreIntegration implements ProductService, RecommendationService, ReviewService ***REMOVED***

  private final String PRODUCT_ID_QUERY_PARAM = "?productId=";
  private final WebClient.Builder webClientBuilder;
  private final ObjectMapper mapper;
  private final MessageSources messageSources;
  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;
  private final int productServiceTimeoutSec;
  private WebClient webClient;

  @Autowired
  public StoreIntegration(
          WebClient.Builder webClientBuilder,
          ObjectMapper mapper,
          MessageSources messageSources,
          @Value("$***REMOVED***app.product-service.host***REMOVED***") String productServiceHost,
          @Value("$***REMOVED***app.recommendation-service.host***REMOVED***") String recommendationServiceHost,
          @Value("$***REMOVED***app.review-service.host***REMOVED***") String reviewServiceHost,
          @Value("$***REMOVED***app.product-service.timeoutSec***REMOVED***") int productServiceTimeoutSec) ***REMOVED***

    this.webClientBuilder = webClientBuilder;
    this.mapper = mapper;
    this.messageSources = messageSources;
    this.productServiceTimeoutSec = productServiceTimeoutSec;

    var http = "http://";

    productServiceUrl = http.concat(productServiceHost);
    recommendationServiceUrl = http.concat(recommendationServiceHost);
    reviewServiceUrl = http.concat(reviewServiceHost);
***REMOVED***

  @Override
  public Product createProduct(Product body) ***REMOVED***
    log.debug("Publishing a create event for a new product ***REMOVED******REMOVED***",body.toString());
    messageSources
            .outputProducts()
            .send(withPayload(new Event<>(CREATE, body.getProductId(), body)).build());
    return body;
***REMOVED***

  @Retry(name = "product")
  @CircuitBreaker(name = "product")
  @Override
  public Mono<Product> getProduct(int productId, int delay, int faultPercent) ***REMOVED***

  var url = UriComponentsBuilder
          .fromUriString(productServiceUrl
          .concat("/products/")
          .concat("***REMOVED***productId***REMOVED***?delay=***REMOVED***delay***REMOVED***&faultPercent=***REMOVED***faultPercent***REMOVED***"))
          .build(productId, delay, faultPercent);

    log.debug("Will call the getProduct API on URL: ***REMOVED******REMOVED***", url);

    return getWebClient()
            .get().uri(url)
            .retrieve().bodyToMono(Product.class)
            .onErrorMap(WebClientResponseException.class, this::handleException)
            .timeout(Duration.ofSeconds(productServiceTimeoutSec));
***REMOVED***

  @Override
  public void deleteProduct(int productId) ***REMOVED***
    log.debug("Publishing a delete event for product id ***REMOVED******REMOVED***", productId);
    messageSources
            .outputProducts()
            .send(withPayload(new Event<>(DELETE, productId, null)).build());
***REMOVED***

  @Override
  public Recommendation createRecommendation(Recommendation body) ***REMOVED***
    log.debug("Publishing a create event for a new recommendation ***REMOVED******REMOVED***",body.toString());

    messageSources
            .outputRecommendations()
            .send(withPayload(new Event<>(CREATE, body.getProductId(), body)).build());

    return body;
***REMOVED***

  @Override
  public Flux<Recommendation> getRecommendations(int productId) ***REMOVED***

    var url = recommendationServiceUrl
            .concat("/recommendations")
            .concat(PRODUCT_ID_QUERY_PARAM)
            .concat(valueOf(productId));

    log.debug("Will call the getRecommendations API on URL: ***REMOVED******REMOVED***", url);

    /* Return an empty result if something goes wrong to make it possible
       for the composite service to return partial responses
    */
    return getWebClient()
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Recommendation.class)
            .log()
            .onErrorResume(error -> empty());
***REMOVED***

  @Override
  public void deleteRecommendations(int productId) ***REMOVED***
    messageSources
            .outputRecommendations()
            .send(withPayload(new Event<>(DELETE, productId, null)).build());
***REMOVED***

  @Override
  public Review createReview(Review body) ***REMOVED***
    messageSources
            .outputReviews()
            .send(withPayload(new Event<>(CREATE, body.getProductId(), body)).build());
    return body;
***REMOVED***

  @Override
  public Flux<Review> getReviews(int productId) ***REMOVED***

    var url = reviewServiceUrl
            .concat("/reviews")
            .concat(PRODUCT_ID_QUERY_PARAM)
            .concat(valueOf(productId));

    log.debug("Will call the getReviews API on URL: ***REMOVED******REMOVED***", url);

    /* Return an empty result if something goes wrong to make it possible
       for the composite service to return partial responses
    */
    return getWebClient()
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Review.class).log()
            .onErrorResume(error -> empty());

***REMOVED***

  @Override
  public void deleteReviews(int productId) ***REMOVED***
    messageSources
            .outputReviews()
            .send(withPayload(new Event<>(DELETE, productId, null)).build());
***REMOVED***

  private WebClient getWebClient() ***REMOVED***
    if (webClient == null) ***REMOVED***
      webClient = webClientBuilder.build();
***REMOVED***
    return webClient;
***REMOVED***

  private Throwable handleException(Throwable ex) ***REMOVED***
    if (!(ex instanceof WebClientResponseException wcre)) ***REMOVED***
      log.warn("Got a unexpected error: ***REMOVED******REMOVED***, will rethrow it", ex.toString());
      return ex;
***REMOVED***

    return switch (wcre.getStatusCode()) ***REMOVED***
      case NOT_FOUND -> new NotFoundException(getErrorMessage(wcre));
      case UNPROCESSABLE_ENTITY -> new InvalidInputException(getErrorMessage(wcre));
      default -> ***REMOVED***
        log.warn("Got a unexpected HTTP error: ***REMOVED******REMOVED***, will rethrow it", wcre.getStatusCode());
        log.warn("Error body: ***REMOVED******REMOVED***", wcre.getResponseBodyAsString());
      throw wcre;***REMOVED***
***REMOVED***;
***REMOVED***

  private String getErrorMessage(WebClientResponseException ex) ***REMOVED***
    try ***REMOVED***
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).message();
***REMOVED*** catch (IOException ioException) ***REMOVED***
      return ex.getMessage();
***REMOVED***
***REMOVED***

  public interface MessageSources ***REMOVED***

    String OUTPUT_PRODUCTS = "output-products";
    String OUTPUT_RECOMMENDATIONS = "output-recommendations";
    String OUTPUT_REVIEWS = "output-reviews";

    @Output(OUTPUT_PRODUCTS)
    MessageChannel outputProducts();

    @Output(OUTPUT_RECOMMENDATIONS)
    MessageChannel outputRecommendations();

    @Output(OUTPUT_REVIEWS)
    MessageChannel outputReviews();
***REMOVED***
***REMOVED***
