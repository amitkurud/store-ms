package com.siriusxi.ms.store.pcs.service;

import com.siriusxi.ms.store.api.composite.StoreService;
import com.siriusxi.ms.store.api.composite.dto.ProductAggregate;
import com.siriusxi.ms.store.api.composite.dto.RecommendationSummary;
import com.siriusxi.ms.store.api.composite.dto.ReviewSummary;
import com.siriusxi.ms.store.api.composite.dto.ServiceAddresses;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.api.core.review.dto.Review;
import com.siriusxi.ms.store.pcs.integration.StoreIntegration;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service("StoreServiceImpl")
@Log4j2
public class StoreServiceImpl implements StoreService ***REMOVED***

  private final ServiceUtil serviceUtil;
  private final StoreIntegration integration;

  @Autowired
  public StoreServiceImpl(ServiceUtil serviceUtil, StoreIntegration integration) ***REMOVED***
    this.serviceUtil = serviceUtil;
    this.integration = integration;
***REMOVED***

  @Override
  public void createProduct(ProductAggregate body) ***REMOVED***

    try ***REMOVED***

      log.debug(
          "createCompositeProduct: creates a new composite entity for productId: ***REMOVED******REMOVED***",
          body.getProductId());

      var product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
      integration.createProduct(product);

      if (body.getRecommendations() != null) ***REMOVED***
        body.getRecommendations()
            .forEach(
                r -> ***REMOVED***
                  var recommendation =
                      new Recommendation(
                          body.getProductId(),
                          r.getRecommendationId(),
                          r.getAuthor(),
                          r.getRate(),
                          r.getContent(),
                          null);
                  integration.createRecommendation(recommendation);
            ***REMOVED***);
  ***REMOVED***

      if (body.getReviews() != null) ***REMOVED***
        body.getReviews()
            .forEach(
                r -> ***REMOVED***
                  Review review =
                      new Review(
                          body.getProductId(),
                          r.getReviewId(),
                          r.getAuthor(),
                          r.getSubject(),
                          r.getContent(),
                          null);
                  integration.createReview(review);
            ***REMOVED***);
  ***REMOVED***
      log.debug(
          "createCompositeProduct: composite entities created for productId: ***REMOVED******REMOVED***",
          body.getProductId());

***REMOVED*** catch (RuntimeException re) ***REMOVED***
      log.warn("createCompositeProduct failed: ***REMOVED******REMOVED***", re.toString());
      throw re;
***REMOVED***
***REMOVED***

  @Override
  public Mono<ProductAggregate> getProduct(int productId) ***REMOVED***
    return Mono.zip(
            values ->
                createProductAggregate(
                    (Product) values[0],
                    (List<Recommendation>) values[1],
                    (List<Review>) values[2],
                    serviceUtil.getServiceAddress()),
            integration.getProduct(productId),
            integration.getRecommendations(productId).collectList(),
            integration.getReviews(productId).collectList())
        .doOnError(ex -> log.warn("getCompositeProduct failed: ***REMOVED******REMOVED***", ex.toString()))
        .log();
***REMOVED***

  @Override
  public void deleteProduct(int productId) ***REMOVED***

    try ***REMOVED***

      log.debug("deleteCompositeProduct: Deletes a product aggregate for productId: ***REMOVED******REMOVED***", productId);

      integration.deleteProduct(productId);
      integration.deleteRecommendations(productId);
      integration.deleteReviews(productId);

      log.debug("deleteCompositeProduct: aggregate entities deleted for productId: ***REMOVED******REMOVED***", productId);

***REMOVED*** catch (RuntimeException re) ***REMOVED***
      log.warn("deleteCompositeProduct failed: ***REMOVED******REMOVED***", re.toString());
      throw re;
***REMOVED***
***REMOVED***

  private ProductAggregate createProductAggregate(
      Product product,
      List<Recommendation> recommendations,
      List<Review> reviews,
      String serviceAddress) ***REMOVED***

    // 1. Setup product info
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries =
        (recommendations == null)
            ? null
            : recommendations.stream()
                .map(
                    r ->
                        new RecommendationSummary(
                            r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
                .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries =
        (reviews == null)
            ? null
            : reviews.stream()
                .map(
                    r ->
                        new ReviewSummary(
                            r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
                .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress =
        (reviews != null && ! reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress =
        (recommendations != null && ! recommendations.isEmpty())
            ? recommendations.get(0).getServiceAddress()
            : "";
    ServiceAddresses serviceAddresses =
        new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(
        productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
***REMOVED***
***REMOVED***
