package com.siriusxi.ms.store.api.core.product;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@RequestMapping("products")
public interface ProductService ***REMOVED***

  /**
   * Sample usage: curl $HOST:$PORT/products/1
   *
   * @param productId is the product that you are looking for.
   * @return the product, if found, else null.
   */
  @GetMapping(value = "products/***REMOVED***productId***REMOVED***",
          produces = APPLICATION_JSON_VALUE)
  Product getProduct(@PathVariable int productId);

  /**
   * Sample usage:
   *
   * <p>curl -X POST $HOST:$PORT/products \ -H "Content-Type: application/json" --data \
   * '***REMOVED***"productId":123,"name":"product 123","weight":123***REMOVED***'
   *
   * @param body product to save.
   * @return just created product.
   */
  @PostMapping( value = "products",
          produces = APPLICATION_JSON_VALUE,
          consumes = APPLICATION_JSON_VALUE)
  Product createProduct(@RequestBody Product body);

  /**
   * Sample usage:
   *
   * <p>curl -X DELETE $HOST:$PORT/products/1
   *
   * @param productId to be deleted.
   */
  @DeleteMapping("products/***REMOVED***productId***REMOVED***")
  void deleteProduct(@PathVariable int productId);
***REMOVED***
