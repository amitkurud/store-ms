package com.siriusxi.ms.store.ps;

import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.ps.persistence.ProductRepository;
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
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.siriusxi.ms.store.api.event.Event.Type.CREATE;
import static com.siriusxi.ms.store.api.event.Event.Type.DELETE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"spring.data.mongodb.port: 0"***REMOVED***)
class ProductServiceApplicationTests ***REMOVED***

  private final String BASE_URI = "/products/";

  @Autowired
  private WebTestClient client;

  @Autowired
  private ProductRepository repository;

  @Autowired
  private Sink channels;

  private AbstractMessageChannel input = null;

  @BeforeEach
  public void setupDb() ***REMOVED***
    input = (AbstractMessageChannel) channels.input();
    repository.deleteAll().block();
***REMOVED***

  @Test
  public void getProductById() ***REMOVED***

    int productId = 1;

    assertNull(repository.findByProductId(productId).block());
    assertEquals(0, repository.count().block());

    sendCreateProductEvent(productId);

    assertNotNull(repository.findByProductId(productId).block());
    assertEquals(1, repository.count().block());

    getAndVerifyProduct(productId, OK)
            .jsonPath("$.productId").isEqualTo(productId);
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    int productId = 1;

    assertNull(repository.findByProductId(productId).block());

    sendCreateProductEvent(productId);

    assertNotNull(repository.findByProductId(productId).block());

    try ***REMOVED***
      sendCreateProductEvent(productId);
      fail("Expected a MessagingException here!");
***REMOVED*** catch (MessagingException me) ***REMOVED***
      if (me.getCause() instanceof InvalidInputException iie)	***REMOVED***
        assertEquals("Duplicate key, Product Id: " + productId, iie.getMessage());
  ***REMOVED*** else ***REMOVED***
        fail("Expected a InvalidInputException as the root cause!");
  ***REMOVED***
***REMOVED***
***REMOVED***

  @Test
  public void deleteProduct() ***REMOVED***

    int productId = 1;

    sendCreateProductEvent(productId);
    assertNotNull(repository.findByProductId(productId).block());

    sendDeleteProductEvent(productId);
    assertNull(repository.findByProductId(productId).block());
***REMOVED***

  @Test
  public void getProductInvalidParameterString() ***REMOVED***

    getAndVerifyProduct(BASE_URI + "/no-integer", BAD_REQUEST)
        .jsonPath("$.path").isEqualTo(BASE_URI + "no-integer")
        .jsonPath("$.message").isEqualTo("Type mismatch.");
***REMOVED***

  @Test
  public void getProductNotFound() ***REMOVED***

    int productIdNotFound = 13;
    getAndVerifyProduct(productIdNotFound, NOT_FOUND)
        .jsonPath("$.path").isEqualTo(BASE_URI + productIdNotFound)
        .jsonPath("$.message")
            .isEqualTo("No product found for productId: " + productIdNotFound);
***REMOVED***

  @Test
  public void getProductInvalidParameterNegativeValue() ***REMOVED***

    int productIdInvalid = -1;

    getAndVerifyProduct(productIdInvalid, UNPROCESSABLE_ENTITY)
        .jsonPath("$.path").isEqualTo(BASE_URI + productIdInvalid)
        .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
***REMOVED***

  private WebTestClient.BodyContentSpec getAndVerifyProduct(
      int productId, HttpStatus expectedStatus) ***REMOVED***
    return getAndVerifyProduct(BASE_URI + productId, expectedStatus);
***REMOVED***

  private WebTestClient.BodyContentSpec getAndVerifyProduct(
      String productIdPath, HttpStatus expectedStatus) ***REMOVED***
    return client
        .get()
        .uri(productIdPath)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private void sendCreateProductEvent(int productId) ***REMOVED***
    Product product = new Product(productId, "Name " + productId, productId, "SA");
    Event<Integer, Product> event = new Event<>(CREATE, productId, product);
    input.send(new GenericMessage<>(event));
***REMOVED***

  private void sendDeleteProductEvent(int productId) ***REMOVED***
    Event<Integer, Product> event = new Event<>(DELETE, productId, null);
    input.send(new GenericMessage<>(event));
***REMOVED***
***REMOVED***
