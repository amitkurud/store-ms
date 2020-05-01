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

import static reactor.core.publisher.Mono.error;

@Service("ProductServiceImpl")
@Log4j2
public class ProductServiceImpl implements ProductService ***REMOVED***

  private final ServiceUtil serviceUtil;

  private final ProductRepository repository;

  private final ProductMapper mapper;

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
  public Mono<Product> getProduct(int productId) ***REMOVED***

    isValidProductId(productId);

    return repository
            .findByProductId(productId)
            .switchIfEmpty(error(new NotFoundException("No product found for productId: " + productId)))
            .log()
            .map(mapper::entityToApi)
            .map(e -> ***REMOVED***
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

    repository
            .findByProductId(productId)
            .log()
            .map(repository::delete)
            .flatMap(e -> e)
            .block();
***REMOVED***

  // TODO could be added to a utility class to be used by all core services implementations.
  private void isValidProductId(int productId) ***REMOVED***
    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
***REMOVED***
***REMOVED***
