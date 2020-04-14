package com.siriusxi.ms.store.ps.api;

import com.siriusxi.ms.store.api.core.product.ProductEndpoint;
import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ProductController</code> is the implementation of the main Product Endpoint API
 * definition.
 *
 * @see ProductEndpoint
 * @author mohamed.taman
 * @version v1.0
 * @since v3.0 codename Storm
 */
@RestController
@Log4j2
public class ProductController implements ProductEndpoint ***REMOVED***

  /** Product service business logic interface. */
  private final ProductService prodService;

  @Autowired
  public ProductController(@Qualifier("ProductServiceImpl") ProductService prodService) ***REMOVED***
    this.prodService = prodService;
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public Product getProduct(int id) ***REMOVED***
    return prodService.getProduct(id);
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public Product createProduct(Product body) ***REMOVED***
    return prodService.createProduct(body);
***REMOVED***

  /** ***REMOVED***@inheritDoc***REMOVED*** */
  @Override
  public void deleteProduct(int id) ***REMOVED***
    prodService.deleteProduct(id);
***REMOVED***
***REMOVED***
