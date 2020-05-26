package com.siriusxi.ms.store.ps.service;

import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.ps.persistence.ProductRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

import static reactor.core.publisher.Mono.error;

@Service("ProductServiceImpl")
@Log4j2
public class ProductServiceImpl implements ProductService ***REMOVED***

  private final ServiceUtil serviceUtil;

  private final ProductRepository repository;

  private final ProductMapper mapper;
  private final Random randomNumberGenerator = new Random();

  @Autowired
  public ProductServiceImpl(
      ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) ***REMOVED***
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
***REMOVED***

  @Override
  public Product createProduct(Product body) ***REMOVED***

    isValidProductId(body.getProductId());

    return repository
        .save(mapper.apiToEntity(body))
        .log()
        .onErrorMap(
            DuplicateKeyException.class,
            ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
        .map(mapper::entityToApi)
        .block();
***REMOVED***

  @Override
  public Mono<Product> getProduct(int productId, int delay, int faultPercent) ***REMOVED***

    isValidProductId(productId);

    if (delay > 0) simulateDelay(delay);

    if (faultPercent > 0) throwErrorIfBadLuck(faultPercent);

    return repository
        .findByProductId(productId)
        .switchIfEmpty(error(new NotFoundException("No product found for productId: " + productId)))
        .log()
        .map(mapper::entityToApi)
        .map(
            e -> ***REMOVED***
              e.setServiceAddress(serviceUtil.getServiceAddress());
              return e;
        ***REMOVED***);
***REMOVED***

  /*
   Implementation is idempotent, that is,
   it will not report any failure if the entity is not found Always 200
  */
  @Override
  public void deleteProduct(int productId) ***REMOVED***

    isValidProductId(productId);

    log.debug("deleteProduct: tries to delete an entity with productId: ***REMOVED******REMOVED***", productId);

    repository.findByProductId(productId).log().map(repository::delete).flatMap(e -> e).block();
***REMOVED***

  // TODO could be added to a utility class to be used by all core services implementations.
  private void isValidProductId(int productId) ***REMOVED***
    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
***REMOVED***

  private void simulateDelay(int delay) ***REMOVED***
    log.debug("Sleeping for ***REMOVED******REMOVED*** seconds***REMOVED***", delay);
    try ***REMOVED***
      Thread.sleep(Duration.ofSeconds(delay).toMillis());
***REMOVED*** catch (InterruptedException ignored) ***REMOVED***
***REMOVED***
    log.debug("Moving on***REMOVED***");
***REMOVED***

  private void throwErrorIfBadLuck(int faultPercent) ***REMOVED***
    int randomThreshold = getRandomNumber(1, 100);
    if (faultPercent < randomThreshold) ***REMOVED***
      log.debug("We got lucky, no error occurred, ***REMOVED******REMOVED*** < ***REMOVED******REMOVED***", faultPercent, randomThreshold);
***REMOVED*** else ***REMOVED***
      log.debug("Bad luck, an error occurred, ***REMOVED******REMOVED*** >= ***REMOVED******REMOVED***", faultPercent, randomThreshold);
      throw new RuntimeException("Something went wrong***REMOVED***");
***REMOVED***
***REMOVED***

  private int getRandomNumber(int min, int max) ***REMOVED***

    if (max < min) ***REMOVED***
      throw new RuntimeException("Max must be greater than min");
***REMOVED***

    return randomNumberGenerator.nextInt((max - min) + 1) + min;
***REMOVED***
***REMOVED***
