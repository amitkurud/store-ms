package com.siriusxi.ms.store.rs;

import com.siriusxi.ms.store.rs.persistence.RecommendationEntity;
import com.siriusxi.ms.store.rs.persistence.RecommendationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PersistenceTests ***REMOVED***

  @Autowired private RecommendationRepository repository;

  private RecommendationEntity savedEntity;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll().block();

    RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");
    savedEntity = repository.save(entity).block();

    assert savedEntity != null;
    assertEqualsRecommendation(entity, savedEntity);
***REMOVED***

  @Test
  public void create() ***REMOVED***

    var newEntity = new RecommendationEntity(1, 3, "a", 3, "c");
    repository.save(newEntity).block();

    var foundEntity = repository.findById(newEntity.getId()).block();

    assert foundEntity != null;
    assertEqualsRecommendation(newEntity, foundEntity);

    assertEquals(2L, repository.count().block());
***REMOVED***

  @Test
  public void update() ***REMOVED***
    savedEntity.setAuthor("a2");
    repository.save(savedEntity).block();

    RecommendationEntity foundEntity = repository.findById(savedEntity.getId()).block();

    assert foundEntity != null;

    assertEquals(1, foundEntity.getVersion());
    assertEquals("a2", foundEntity.getAuthor());
***REMOVED***

  @Test
  public void delete() ***REMOVED***
    repository.delete(savedEntity).block();
    assertEquals(false, repository.existsById(savedEntity.getId()).block());
***REMOVED***

  @Test
  public void getByProductId() ***REMOVED***
    List<RecommendationEntity> entityList =
            repository.findByProductId(savedEntity.getProductId()).collectList().block();

    assertThat(entityList, hasSize(1));
    assertEqualsRecommendation(savedEntity, entityList.get(0));
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    Assertions.assertThrows(
        DuplicateKeyException.class,
        () -> repository
                .save(new RecommendationEntity(1, 2, "a", 3, "c"))
                .block());
***REMOVED***

  @Test
  public void optimisticLockError() ***REMOVED***

    // Store the saved entity in two separate entity objects
    RecommendationEntity entity1 = repository.findById(savedEntity.getId()).block(),
                         entity2 = repository.findById(savedEntity.getId()).block();

    // Update the entity using the first entity object
    assert entity1 != null;

    entity1.setAuthor("a1");
    repository.save(entity1).block();

    /*
      Update the entity using the second entity object.
      This should fail since the second entity now holds a old version number,
      i.e. a Optimistic Lock Error.
    */
    try ***REMOVED***
      assert entity2 != null;

      entity2.setAuthor("a2");
      repository.save(entity2).block();

      fail("Expected an OptimisticLockingFailureException");
***REMOVED*** catch (OptimisticLockingFailureException ignored) ***REMOVED***
***REMOVED***

    // Get the updated entity from the database and verify its new sate
    var updatedEntity = repository.findById(savedEntity.getId()).block();

    assert updatedEntity != null;

    assertEquals(1, updatedEntity.getVersion());
    assertEquals("a1", updatedEntity.getAuthor());
***REMOVED***

  private void assertEqualsRecommendation(
      RecommendationEntity expectedEntity, RecommendationEntity actualEntity) ***REMOVED***
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getRecommendationId(), actualEntity.getRecommendationId());
    assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
    assertEquals(expectedEntity.getRating(), actualEntity.getRating());
    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
***REMOVED***
***REMOVED***
