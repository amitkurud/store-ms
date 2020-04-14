package com.siriusxi.ms.store.ps;

import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.ps.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ***REMOVED***"spring.data.mongodb.port: 0"***REMOVED***)
class ProductServiceApplicationTests ***REMOVED***

  private final String BASE_URI = "/products/";

  @Autowired private WebTestClient client;

  @Autowired private ProductRepository repository;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll();
***REMOVED***

  @Test
  public void getProductById() ***REMOVED***

    int productId = 1;

    postAndVerifyProduct(productId, OK);

    assertTrue(repository.findByProductId(productId).isPresent());

    getAndVerifyProduct(productId, OK).jsonPath("$.productId").isEqualTo(productId);
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    int productId = 1;

    postAndVerifyProduct(productId, OK);

    assertTrue(repository.findByProductId(productId).isPresent());

    postAndVerifyProduct(productId, UNPROCESSABLE_ENTITY)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI)
        .jsonPath("$.message")
        .isEqualTo("Duplicate key, Product Id: " + productId);
***REMOVED***

  @Test
  public void deleteProduct() ***REMOVED***

    int productId = 1;

    postAndVerifyProduct(productId, OK);
    assertTrue(repository.findByProductId(productId).isPresent());

    deleteAndVerifyProduct(productId);
    assertFalse(repository.findByProductId(productId).isPresent());

    deleteAndVerifyProduct(productId);
***REMOVED***

  @Test
  public void getProductInvalidParameterString() ***REMOVED***

    getAndVerifyProduct(BASE_URI + "/no-integer", BAD_REQUEST)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI + "no-integer")
        .jsonPath("$.message")
        .isEqualTo("Type mismatch.");
***REMOVED***

  @Test
  public void getProductNotFound() ***REMOVED***

    int productIdNotFound = 13;
    getAndVerifyProduct(productIdNotFound, NOT_FOUND)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI + productIdNotFound)
        .jsonPath("$.message")
        .isEqualTo("No product found for productId: " + productIdNotFound);
***REMOVED***

  @Test
  public void getProductInvalidParameterNegativeValue() ***REMOVED***

    int productIdInvalid = -1;

    getAndVerifyProduct(productIdInvalid, UNPROCESSABLE_ENTITY)
        .jsonPath("$.path")
        .isEqualTo(BASE_URI + productIdInvalid)
        .jsonPath("$.message")
        .isEqualTo("Invalid productId: " + productIdInvalid);
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
        .expectStatus()
        .isEqualTo(expectedStatus)
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private WebTestClient.BodyContentSpec postAndVerifyProduct(
      int productId, HttpStatus expectedStatus) ***REMOVED***
    Product product = new Product(productId, "Name " + productId, productId, "SA");
    return client
        .post()
        .uri(BASE_URI)
        .body(Mono.just(product), Product.class)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(expectedStatus)
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody();
***REMOVED***

  private void deleteAndVerifyProduct(int productId) ***REMOVED***
    client
        .delete()
        .uri(BASE_URI + productId)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(OK)
        .expectBody();
***REMOVED***
***REMOVED***
