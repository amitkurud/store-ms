package com.siriusxi.ms.store.ps;

import com.siriusxi.ms.store.api.core.product.Product;
import com.siriusxi.ms.store.ps.controller.ProductMapper;
import com.siriusxi.ms.store.ps.persistence.ProductEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapperTests ***REMOVED***

  private final ProductMapper mapper = ProductMapper.INSTANCE;

  @Test
  public void mapperTests() ***REMOVED***

    assertNotNull(mapper);

    Product api = new Product(1, "n", 1, "sa");

    ProductEntity entity = mapper.apiToEntity(api);

    assertEquals(api.getProductId(), entity.getProductId());
    assertEquals(api.getProductId(), entity.getProductId());
    assertEquals(api.getName(), entity.getName());
    assertEquals(api.getWeight(), entity.getWeight());

    Product api2 = mapper.entityToApi(entity);

    assertEquals(api.getProductId(), api2.getProductId());
    assertEquals(api.getProductId(), api2.getProductId());
    assertEquals(api.getName(), api2.getName());
    assertEquals(api.getWeight(), api2.getWeight());
    assertNull(api2.getServiceAddress());
***REMOVED***
***REMOVED***