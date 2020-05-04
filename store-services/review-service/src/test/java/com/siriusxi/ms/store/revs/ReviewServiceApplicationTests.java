package com.siriusxi.ms.store.revs;

import com.siriusxi.ms.store.api.core.review.dto.Review;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.revs.persistence.ReviewRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.siriusxi.ms.store.api.event.Event.Type.CREATE;
import static com.siriusxi.ms.store.api.event.Event.Type.DELETE;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"spring.datasource.url=jdbc:h2:mem:review-db"***REMOVED***)
@ActiveProfiles("test")
class ReviewServiceApplicationTests ***REMOVED***

  private final String BASE_URI = "/reviews";

  @Autowired private WebTestClient client;

  @Autowired private ReviewRepository repository;

  @Autowired
  private Sink channels;

  private AbstractMessageChannel input = null;

  @BeforeEach
  public void setupDb() ***REMOVED***
    input = (AbstractMessageChannel) channels.input();
    repository.deleteAll();
***REMOVED***

  @Test
  public void getReviewsByProductId() ***REMOVED***

    int productId = 1;

    assertEquals(0, repository.findByProductId(productId).size());

    sendCreateReviewEvent(productId, 1);
    sendCreateReviewEvent(productId, 2);
    sendCreateReviewEvent(productId, 3);

    assertEquals(3, repository.findByProductId(productId).size());

    getAndVerifyReviewsByProductId(productId)
        .jsonPath("$.length()").isEqualTo(3)
        .jsonPath("$[2].productId").isEqualTo(productId)
        .jsonPath("$[2].reviewId").isEqualTo(3);
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    int productId = 1;
    int reviewId = 1;

    assertEquals(0, repository.count());

    sendCreateReviewEvent(productId, reviewId);

    assertEquals(1, repository.count());

    try ***REMOVED***
      sendCreateReviewEvent(productId, reviewId);
      fail("Expected a MessagingException here!");
***REMOVED*** catch (MessagingException me) ***REMOVED***
      if (me.getCause() instanceof InvalidInputException iie)	***REMOVED***
        assertEquals("Duplicate key, Product Id: 1, Review Id:1", iie.getMessage());
  ***REMOVED*** else ***REMOVED***
        fail("Expected a InvalidInputException as the root cause!");
  ***REMOVED***
***REMOVED***

    assertEquals(1, repository.count());
***REMOVED***

  @Test
  public void deleteReviews() ***REMOVED***

    int productId = 1;
    int reviewId = 1;

    sendCreateReviewEvent(productId, reviewId);
    assertEquals(1, repository.findByProductId(productId).size());

    sendDeleteReviewEvent(productId);
    assertEquals(0, repository.findByProductId(productId).size());

    sendDeleteReviewEvent(productId);
***REMOVED***

  @Test
  public void getReviewsMissingParameter() ***REMOVED***

    getAndVerifyReviewsByProductId("", BAD_REQUEST)
        .jsonPath("$.path").isEqualTo(BASE_URI)
        .jsonPath("$.message")
            .isEqualTo("Required int parameter 'productId' is not present");
***REMOVED***

  @Test
  public void getReviewsInvalidParameter() ***REMOVED***

    getAndVerifyReviewsByProductId("?productId=no-integer", BAD_REQUEST)
        .jsonPath("$.path").isEqualTo(BASE_URI)
        .jsonPath("$.message").isEqualTo("Type mismatch.");
***REMOVED***

  @Test
  public void getReviewsNotFound() ***REMOVED***

    getAndVerifyReviewsByProductId("?productId=213", OK)
            .jsonPath("$.length()").isEqualTo(0);
***REMOVED***

  @Test
  public void getReviewsInvalidParameterNegativeValue() ***REMOVED***

    int productIdInvalid = -1;

    getAndVerifyReviewsByProductId("?productId=" + productIdInvalid,
            UNPROCESSABLE_ENTITY)
        .jsonPath("$.path").isEqualTo(BASE_URI)
        .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
***REMOVED***

  private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(
          int productId) ***REMOVED***
    return getAndVerifyReviewsByProductId("?productId=" + productId, HttpStatus.OK);
***REMOVED***

  private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(
      String productIdQuery, HttpStatus expectedStatus) ***REMOVED***
    return client
        .get()
        .uri(BASE_URI + productIdQuery)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private void sendCreateReviewEvent(int productId, int reviewId) ***REMOVED***
    Review review = new Review(productId, reviewId, "Author " + reviewId,
            "Subject " + reviewId, "Content " + reviewId, "SA");
    Event<Integer, Review> event = new Event<>(CREATE, productId, review);
    input.send(new GenericMessage<>(event));
***REMOVED***

  private void sendDeleteReviewEvent(int productId) ***REMOVED***
    Event<Integer, Review> event = new Event<>(DELETE, productId, null);
    input.send(new GenericMessage<>(event));
***REMOVED***
***REMOVED***
