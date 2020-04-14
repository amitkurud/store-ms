package com.siriusxi.ms.store.rs;

import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.rs.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"spring.data.mongodb.port: 0"***REMOVED***)
class RecommendationServiceApplicationTests ***REMOVED***

  private final String BASE_URI = "/recommendations";
  @Autowired private WebTestClient client;

  @Autowired private RecommendationRepository repository;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll();
***REMOVED***

  @Test
  public void getRecommendationsByProductId() ***REMOVED***

    int productId = 1;

    postAndVerifyRecommendation(productId, 1, OK);
    postAndVerifyRecommendation(productId, 2, OK);
    postAndVerifyRecommendation(productId, 3, OK);

    assertEquals(3, repository.findByProductId(productId).size());

    getAndVerifyRecommendationsByProductId(productId, OK)
        .jsonPath("$.length()")
        .isEqualTo(3)
        .jsonPath("$[2].productId")
        .isEqualTo(productId)
        .jsonPath("$[2].recommendationId")
        .isEqualTo(3);
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    int productId = 1;
    int recommendationId = 1;

    postAndVerifyRecommendation(productId, recommendationId, OK)
        .jsonPath("$.productId")
        .isEqualTo(productId)
        .jsonPath("$.recommendationId")
        .isEqualTo(recommendationId);

    assertEquals(1, repository.count());

    postAndVerifyRecommendation(productId, recommendationId, UNPROCESSABLE_ENTITY)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI)
        .jsonPath("$.message")
        .isEqualTo("Duplicate key, Product Id: 1, Recommendation Id:1");

    assertEquals(1, repository.count());
***REMOVED***

  @Test
  public void deleteRecommendations() ***REMOVED***

    int productId = 1;
    int recommendationId = 1;

    postAndVerifyRecommendation(productId, recommendationId, OK);
    assertEquals(1, repository.findByProductId(productId).size());

    deleteAndVerifyRecommendationsByProductIdIsOk(productId);
    assertEquals(0, repository.findByProductId(productId).size());

    deleteAndVerifyRecommendationsByProductIdIsOk(productId);
***REMOVED***

  @Test
  public void getRecommendationsMissingParameter() ***REMOVED***

    getAndVerifyRecommendationsByProductId("", BAD_REQUEST)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI)
        .jsonPath("$.message")
        .isEqualTo("Required int parameter 'productId' is not present");
***REMOVED***

  @Test
  public void getRecommendationsInvalidParameter() ***REMOVED***

    getAndVerifyRecommendationsByProductId("?productId=no-integer", BAD_REQUEST)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI)
        .jsonPath("$.message")
        .isEqualTo("Type mismatch.");
***REMOVED***

  @Test
  public void getRecommendationsNotFound() ***REMOVED***

    getAndVerifyRecommendationsByProductId("?productId=113", OK)
        .jsonPath("$.length()")
        .isEqualTo(0);
***REMOVED***

  @Test
  public void getRecommendationsInvalidParameterNegativeValue() ***REMOVED***

    int productIdInvalid = -1;

    getAndVerifyRecommendationsByProductId("?productId=" + productIdInvalid, UNPROCESSABLE_ENTITY)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI)
        .jsonPath("$.message")
        .isEqualTo("Invalid productId: " + productIdInvalid);
***REMOVED***

  private BodyContentSpec getAndVerifyRecommendationsByProductId(
      int productId, HttpStatus expectedStatus) ***REMOVED***
    return getAndVerifyRecommendationsByProductId("?productId=" + productId, expectedStatus);
***REMOVED***

  private BodyContentSpec getAndVerifyRecommendationsByProductId(
      String productIdQuery, HttpStatus expectedStatus) ***REMOVED***
    return client
        .get()
        .uri(BASE_URI + productIdQuery)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(expectedStatus)
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private BodyContentSpec postAndVerifyRecommendation(
      int productId, int recommendationId, HttpStatus expectedStatus) ***REMOVED***

    Recommendation recommendation =
        new Recommendation(
            productId,
            recommendationId,
            "Author " + recommendationId,
            recommendationId,
            "Content " + recommendationId,
            "SA");

    return client
        .post()
        .uri(BASE_URI)
        .body(Mono.just(recommendation), Recommendation.class)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(expectedStatus)
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private void deleteAndVerifyRecommendationsByProductIdIsOk(int productId) ***REMOVED***
    client
        .delete()
        .uri(BASE_URI + "?productId=" + productId)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(OK)
        .expectBody();
***REMOVED***
***REMOVED***
