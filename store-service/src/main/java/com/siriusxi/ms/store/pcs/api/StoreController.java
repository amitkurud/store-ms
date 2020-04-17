package com.siriusxi.ms.store.pcs.api;

import com.siriusxi.ms.store.api.composite.StoreEndpoint;
import com.siriusxi.ms.store.api.composite.StoreService;
import com.siriusxi.ms.store.api.composite.dto.ProductAggregate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class StoreController implements StoreEndpoint ***REMOVED***
  /** Store service business logic interface. */
  private final StoreService storeService;

  @Autowired
  public StoreController(@Qualifier("StoreServiceImpl") StoreService storeService) ***REMOVED***
    this.storeService = storeService;
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public ProductAggregate getProduct(int id) ***REMOVED***
    return storeService.getProduct(id);
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public void createProduct(ProductAggregate body) ***REMOVED***
    storeService.createProduct(body);
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public void deleteProduct(int id) ***REMOVED***
    storeService.deleteProduct(id);
***REMOVED***
***REMOVED***
