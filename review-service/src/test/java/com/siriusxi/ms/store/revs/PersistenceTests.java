package com.siriusxi.ms.store.revs;

import com.siriusxi.ms.store.revs.persistence.ReviewEntity;
import com.siriusxi.ms.store.revs.persistence.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests ***REMOVED***

  @Autowired private ReviewRepository repository;

  private ReviewEntity savedEntity;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll();

    ReviewEntity entity = new ReviewEntity(1, 2, "a", "s", "c");
    savedEntity = repository.save(entity);

    assertEqualsReview(entity, savedEntity);
***REMOVED***

  @Test
  public void create() ***REMOVED***

    ReviewEntity newEntity = new ReviewEntity(1, 3, "a", "s", "c");
    repository.save(newEntity);

    ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();
    assertEqualsReview(newEntity, foundEntity);

    assertEquals(2, repository.count());
***REMOVED***

  @Test
  public void update() ***REMOVED***
    savedEntity.setAuthor("a2");
    repository.save(savedEntity);

    ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("a2", foundEntity.getAuthor());
***REMOVED***

  @Test
  public void delete() ***REMOVED***
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
***REMOVED***

  @Test
  public void getByProductId() ***REMOVED***
    List<ReviewEntity> entityList = repository.findByProductId(savedEntity.getProductId());

    assertThat(entityList, hasSize(1));
    assertEqualsReview(savedEntity, entityList.get(0));
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    Assertions.assertThrows(
        DataIntegrityViolationException.class,
        () -> ***REMOVED***
          ReviewEntity entity = new ReviewEntity(1, 2, "a", "s", "c");
          repository.save(entity);
    ***REMOVED***);
***REMOVED***

  @Test
  public void optimisticLockError() ***REMOVED***

    // Store the saved entity in two separate entity objects
    ReviewEntity entity1 = repository.findById(savedEntity.getId()).get();
    ReviewEntity entity2 = repository.findById(savedEntity.getId()).get();

    // Update the entity using the first entity object
    entity1.setAuthor("a1");
    repository.save(entity1);

    //  Update the entity using the second entity object.
    // This should fail since the second entity now holds a old version number, i.e. a Optimistic
    // Lock Error
    try ***REMOVED***
      entity2.setAuthor("a2");
      repository.save(entity2);

      fail("Expected an OptimisticLockingFailureException");
***REMOVED*** catch (OptimisticLockingFailureException ignored) ***REMOVED***
***REMOVED***

    // Get the updated entity from the database and verify its new sate
    ReviewEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (int) updatedEntity.getVersion());
    assertEquals("a1", updatedEntity.getAuthor());
***REMOVED***

  private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) ***REMOVED***
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getReviewId(), actualEntity.getReviewId());
    assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
    assertEquals(expectedEntity.getSubject(), actualEntity.getSubject());
    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
***REMOVED***
***REMOVED***
