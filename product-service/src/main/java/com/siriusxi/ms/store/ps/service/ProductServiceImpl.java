package com.siriusxi.ms.store.ps.service;

import com.mongodb.DuplicateKeyException;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.ps.persistence.ProductEntity;
import com.siriusxi.ms.store.ps.persistence.ProductRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProductServiceImpl")
@Log4j2
public class ProductServiceImpl implements ProductService ***REMOVED***

    private final ServiceUtil serviceUtil;

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository repository,
                              ProductMapper mapper,
                              ServiceUtil serviceUtil) ***REMOVED***
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
***REMOVED***

    @Override
    public Product createProduct(Product body) ***REMOVED***
        try ***REMOVED***
            ProductEntity entity = mapper.apiToEntity(body);
            ProductEntity newEntity = repository.save(entity);

            log.debug("createProduct: entity created for productId: ***REMOVED******REMOVED***", body.getProductId());
            return mapper.entityToApi(newEntity);

    ***REMOVED*** catch (DuplicateKeyException dke) ***REMOVED***
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
    ***REMOVED***
***REMOVED***

    @Override
    public Product getProduct(int productId) ***REMOVED***
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        ProductEntity entity = repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product response = mapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        log.debug("getProduct: found productId: ***REMOVED******REMOVED***", response.getProductId());

        return response;
***REMOVED***

    /*
     Implementation is idempotent, that is,
     it will not report any failure if the entity is not found Always 200
    */
    @Override
    public void deleteProduct(int productId) ***REMOVED***
        log.debug("deleteProduct: tries to delete an entity with productId: ***REMOVED******REMOVED***", productId);
        repository.findByProductId(productId).ifPresent(repository::delete);
***REMOVED***
***REMOVED***
