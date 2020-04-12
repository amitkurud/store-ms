package com.siriusxi.ms.store.ps;

import com.siriusxi.ms.store.ps.persistence.ProductEntity;
import com.siriusxi.ms.store.ps.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

// FIXME to fix all optional class check with isPresent()
@DataMongoTest
public class PersistenceTests ***REMOVED***

  @Autowired private ProductRepository repository;

  private ProductEntity savedEntity;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll();

    ProductEntity entity = new ProductEntity(1, "n", 1);
    savedEntity = repository.save(entity);

    assertEqualsProduct(entity, savedEntity);
***REMOVED***

  @Test
  public void create() ***REMOVED***

    ProductEntity newEntity = new ProductEntity(2, "n", 2);
    repository.save(newEntity);

    ProductEntity foundEntity = repository.findById(newEntity.getId()).get();
    assertEqualsProduct(newEntity, foundEntity);

    assertEquals(2, repository.count());
***REMOVED***

  @Test
  public void update() ***REMOVED***
    savedEntity.setName("n2");
    repository.save(savedEntity);

    ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("n2", foundEntity.getName());
***REMOVED***

  @Test
  public void delete() ***REMOVED***
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
***REMOVED***

  @Test
  public void getByProductId() ***REMOVED***
    Optional<ProductEntity> entity = repository.findByProductId(savedEntity.getProductId());

    assertTrue(entity.isPresent());
    assertEqualsProduct(savedEntity, entity.get());
***REMOVED***

  //FIXME error which is not thrown
  @Test
  @Disabled
  public void duplicateError() ***REMOVED***

    Assertions.assertThrows(
        DuplicateKeyException.class,
        () -> ***REMOVED***
          ProductEntity entity = new ProductEntity(savedEntity.getProductId(), "n", 1);
          repository.save(entity);
    ***REMOVED***);
***REMOVED***

  @Test
  public void optimisticLockError() ***REMOVED***

    // Store the saved entity in two separate entity objects
    ProductEntity entity1 = repository.findById(savedEntity.getId()).get();
    ProductEntity entity2 = repository.findById(savedEntity.getId()).get();

    // Update the entity using the first entity object
    entity1.setName("n1");
    repository.save(entity1);

    //  Update the entity using the second entity object.
    // This should fail since the second entity now holds a old version number, i.e. a Optimistic
    // Lock Error
    try ***REMOVED***
      entity2.setName("n2");
      repository.save(entity2);

      fail("Expected an OptimisticLockingFailureException");
***REMOVED*** catch (OptimisticLockingFailureException ignored) ***REMOVED***
***REMOVED***

    // Get the updated entity from the database and verify its new sate
    ProductEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (int) updatedEntity.getVersion());
    assertEquals("n1", updatedEntity.getName());
***REMOVED***

  @Test
  public void paging() ***REMOVED***

    repository.deleteAll();

    List<ProductEntity> newProducts =
        rangeClosed(1001, 1010)
            .mapToObj(i -> new ProductEntity(i, "name " + i, i))
            .collect(Collectors.toList());
    repository.saveAll(newProducts);

    Pageable nextPage = PageRequest.of(0, 4, ASC, "productId");
    nextPage = testNextPage(nextPage, "[1001, 1002, 1003, 1004]", true);
    nextPage = testNextPage(nextPage, "[1005, 1006, 1007, 1008]", true);
    testNextPage(nextPage, "[1009, 1010]", false);
***REMOVED***

  private Pageable testNextPage(
      Pageable nextPage, String expectedProductIds, boolean expectsNextPage) ***REMOVED***
    Page<ProductEntity> productPage = repository.findAll(nextPage);
    assertEquals(
        expectedProductIds,
        productPage.getContent().stream()
            .map(ProductEntity::getProductId)
            .collect(Collectors.toList())
            .toString());
    assertEquals(expectsNextPage, productPage.hasNext());
    return productPage.nextPageable();
***REMOVED***

  private void assertEqualsProduct(ProductEntity expectedEntity, ProductEntity actualEntity) ***REMOVED***
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getName(), actualEntity.getName());
    assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());
***REMOVED***
***REMOVED***