package com.siriusxi.ms.store.revs;

import com.siriusxi.ms.store.revs.persistence.ReviewEntity;
import com.siriusxi.ms.store.revs.persistence.ReviewRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersistenceTests ***REMOVED***

  @Autowired private ReviewRepository repository;

  private ReviewEntity savedEntity;

  @BeforeEach
  public void setupDb() ***REMOVED***
    repository.deleteAll();

    ReviewEntity entity = new ReviewEntity(1, 2, "amazon", "s", "c");
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
***REMOVED***

  @Test
  public void create() ***REMOVED***

    ReviewEntity newEntity = new ReviewEntity(1, 3, "amazon 1", "s", "c");
    repository.save(newEntity);

    Optional<ReviewEntity> entity = repository.findById(newEntity.getId());
    ReviewEntity foundEntity = new ReviewEntity();
    if(entity.isPresent()) foundEntity = entity.get();

    assertEquals(newEntity, foundEntity);

    assertEquals(2, repository.count());
***REMOVED***

  @Test
  public void update() ***REMOVED***
    savedEntity.setAuthor("amazon 2");
    repository.save(savedEntity);

    Optional<ReviewEntity> entity = repository.findById(savedEntity.getId());
    ReviewEntity foundEntity = new ReviewEntity();
    if(entity.isPresent()) foundEntity = entity.get();

    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("amazon 2", foundEntity.getAuthor());
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
    assertEquals(savedEntity, entityList.get(0));
***REMOVED***

  @Test
  public void duplicateError() ***REMOVED***

    Assertions.assertThrows(
        DataIntegrityViolationException.class,
        () -> ***REMOVED***
          ReviewEntity entity = new ReviewEntity(1, 2, "amazon 1", "s", "c");
          repository.save(entity);
    ***REMOVED***);
***REMOVED***

  @Test
  public void optimisticLockError() ***REMOVED***

    ReviewEntity entity1 = new ReviewEntity(),
                 entity2 = new ReviewEntity();

    // Store the saved entity in two separate entity objects
    Optional<ReviewEntity> result = repository.findById(savedEntity.getId());
    if (result.isPresent()) entity1 = result.get();

    Optional<ReviewEntity> result2 = repository.findById(savedEntity.getId());
    if (result2.isPresent()) entity2 = result2.get();


    // Update the entity using the first entity object
    entity1.setAuthor("amazon 1");
    repository.save(entity1);

    //  Update the entity using the second entity object.
    // This should fail since the second entity now holds a old version number, i.e. a Optimistic
    // Lock Error
    try ***REMOVED***
      entity2.setAuthor("amazon 2");
      repository.save(entity2);

      fail("Expected an OptimisticLockingFailureException");
***REMOVED*** catch (OptimisticLockingFailureException ignored) ***REMOVED***
***REMOVED***

    // Get the updated entity from the database and verify its new sate
    Optional<ReviewEntity> foundEntity = repository.findById(savedEntity.getId());
    ReviewEntity updatedEntity = new ReviewEntity();
    if(foundEntity.isPresent()) updatedEntity = foundEntity.get();

    assertEquals(1, updatedEntity.getVersion());
    assertEquals("amazon 1", updatedEntity.getAuthor());
***REMOVED***
***REMOVED***
