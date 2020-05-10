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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.ReactiveSecurityContextHolder.getContext;

@Service("StoreServiceImpl")
@Log4j2
public class StoreServiceImpl implements StoreService ***REMOVED***

  private final ServiceUtil serviceUtil;
  private final StoreIntegration integration;
  private final SecurityContext nullSC = new SecurityContextImpl();

  @Autowired
  public StoreServiceImpl(ServiceUtil serviceUtil, StoreIntegration integration) ***REMOVED***
    this.serviceUtil = serviceUtil;
    this.integration = integration;
***REMOVED***

  @Override
  public Mono<Void> createProduct(ProductAggregate body) ***REMOVED***
    return getContext().doOnSuccess(sc -> createProductImpl(sc, body)).then();
***REMOVED***

  private void createProductImpl(SecurityContext sc, ProductAggregate body) ***REMOVED***
    try ***REMOVED***

      log.debug(
          "createProduct: creates a new composite entity for productId: ***REMOVED******REMOVED***", body.productId());

      logAuthorizationInfo(sc);

      var product = new Product(body.productId(), body.name(), body.weight(), null);
      integration.createProduct(product);

      if (body.recommendations() != null && !body.recommendations().isEmpty()) ***REMOVED***
        body.recommendations()
            .forEach(
                r -> ***REMOVED***
                  var recommendation =
                      new Recommendation(
                          body.productId(),
                          r.recommendationId(),
                          r.author(),
                          r.rate(),
                          r.content(),
                          null);
                  integration.createRecommendation(recommendation);
            ***REMOVED***);
  ***REMOVED***

      if (body.reviews() != null && !body.reviews().isEmpty()) ***REMOVED***
        body.reviews()
            .forEach(
                r -> ***REMOVED***
                  Review review =
                      new Review(
                          body.productId(),
                          r.reviewId(),
                          r.author(),
                          r.subject(),
                          r.content(),
                          null);
                  integration.createReview(review);
            ***REMOVED***);
  ***REMOVED***
      log.debug(
          "createProduct: composite entities created for productId: ***REMOVED******REMOVED***", body.productId());

***REMOVED*** catch (RuntimeException re) ***REMOVED***
      log.warn("createProduct failed: ***REMOVED******REMOVED***", re.toString());
      throw re;
***REMOVED***
***REMOVED***

  @Override
  public Mono<ProductAggregate> getProduct(int productId) ***REMOVED***
    return Mono.zip(
            values ->
                createProductAggregate(
                    (SecurityContext) values[0],
                    (Product) values[1],
                    (List<Recommendation>) values[2],
                    (List<Review>) values[3],
                    serviceUtil.getServiceAddress()),
            getContext().defaultIfEmpty(nullSC),
            integration.getProduct(productId),
            integration.getRecommendations(productId).collectList(),
            integration.getReviews(productId).collectList())
        .doOnError(ex -> log.warn("getProduct failed: ***REMOVED******REMOVED***", ex.toString()))
        .log();
***REMOVED***

  @Override
  public Mono<Void> deleteProduct(int productId) ***REMOVED***
    return getContext().doOnSuccess(sc -> deleteProductImpl(sc, productId)).then();
***REMOVED***

  private void deleteProductImpl(SecurityContext sc, int productId) ***REMOVED***
    try ***REMOVED***

      log.debug("deleteProduct: Deletes a product aggregate for productId: ***REMOVED******REMOVED***", productId);
      logAuthorizationInfo(sc);

      integration.deleteProduct(productId);
      integration.deleteRecommendations(productId);
      integration.deleteReviews(productId);

      log.debug("deleteProduct: aggregate entities deleted for productId: ***REMOVED******REMOVED***", productId);

***REMOVED*** catch (RuntimeException re) ***REMOVED***
      log.warn("deleteProduct failed: ***REMOVED******REMOVED***", re.toString());
      throw re;
***REMOVED***
***REMOVED***

  private ProductAggregate createProductAggregate(
      SecurityContext sc,
      Product product,
      List<Recommendation> recommendations,
      List<Review> reviews,
      String serviceAddress) ***REMOVED***

    logAuthorizationInfo(sc);

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
        (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress =
        (recommendations != null && !recommendations.isEmpty())
            ? recommendations.get(0).getServiceAddress()
            : "";
    ServiceAddresses serviceAddresses =
        new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(
        productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
***REMOVED***


  /*
   * Methods logAuthorizationInfo(SecurityContext sc),
   * and     logAuthorizationInfo(Jwt jwt)
   * has been added to log relevant parts from the JWT-encoded
   * access token upon each call to the API.
   *
   * The access token can be acquired using the standard Spring Security, SecurityContext,
   * which, in a reactive environment, can be acquired using the static helper method,
   * ReactiveSecurityContextHolder.getContext().
   *
   */
  private void logAuthorizationInfo(SecurityContext sc) ***REMOVED***
    if (sc != null
        && sc.getAuthentication() != null
        && sc.getAuthentication() instanceof JwtAuthenticationToken) ***REMOVED***
      Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
      logAuthorizationInfo(jwtToken);
***REMOVED*** else ***REMOVED***
      log.warn("No JWT based Authentication supplied, running tests are we?");
***REMOVED***
***REMOVED***

  private void logAuthorizationInfo(Jwt jwt) ***REMOVED***
    if (jwt == null) ***REMOVED***
      log.warn("No JWT supplied, running tests are we?");
***REMOVED*** else ***REMOVED***
      if (log.isDebugEnabled()) ***REMOVED***
        URL issuer = jwt.getIssuer();
        List<String> audience = jwt.getAudience();
        Object subject = jwt.getClaims().get("sub");
        Object scopes = jwt.getClaims().get("scope");
        Object expires = jwt.getClaims().get("exp");

        log.debug(
            "Authorization info: Subject: ***REMOVED******REMOVED***, scopes: ***REMOVED******REMOVED***, expires ***REMOVED******REMOVED***: issuer: ***REMOVED******REMOVED***, audience: ***REMOVED******REMOVED***",
            subject,
            scopes,
            expires,
            issuer,
            audience);
  ***REMOVED***
***REMOVED***
***REMOVED***
***REMOVED***
