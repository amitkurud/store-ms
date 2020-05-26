package com.siriusxi.ms.store.pcs.api;

import com.siriusxi.ms.store.api.composite.StoreEndpoint;
import com.siriusxi.ms.store.api.composite.StoreService;
import com.siriusxi.ms.store.api.composite.dto.ProductAggregate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class StoreController implements StoreEndpoint ***REMOVED***

  /** Store service business logic interface. */
  private final StoreService storeService;

  @Autowired
  public StoreController(@Qualifier("StoreServiceImpl") StoreService storeService) ***REMOVED***
    this.storeService = storeService;
***REMOVED***

  /**
   * ***REMOVED***@inheritDoc***REMOVED***
   *
   * @return final product.
   */
  @Override
  public Mono<ProductAggregate> getProduct(int id, int delay, int faultPercent) ***REMOVED***
    return storeService.getProduct(id, delay, faultPercent);
***REMOVED***

  /**
   * ***REMOVED***@inheritDoc***REMOVED***
   *
   * @return nothing.
   */
  @Override
  public Mono<Void> createProduct(ProductAggregate body) ***REMOVED***
    return storeService.createProduct(body);
***REMOVED***

  /**
   * ***REMOVED***@inheritDoc***REMOVED***
   *
   * @return nothing.
   */
  @Override
  public Mono<Void> deleteProduct(int id) ***REMOVED***
    return storeService.deleteProduct(id);
***REMOVED***
***REMOVED***
