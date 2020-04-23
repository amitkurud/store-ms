package com.siriusxi.ms.store.revs.service;

import com.siriusxi.ms.store.api.core.review.ReviewService;
import com.siriusxi.ms.store.api.core.review.dto.Review;
import com.siriusxi.ms.store.revs.persistence.ReviewEntity;
import com.siriusxi.ms.store.revs.persistence.ReviewRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.Supplier;

import static java.util.logging.Level.FINE;

@Service("ReviewServiceImpl")
@Log4j2
public class ReviewServiceImpl implements ReviewService ***REMOVED***

  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final ServiceUtil serviceUtil;
  private final Scheduler scheduler;

  @Autowired
  public ReviewServiceImpl(
          Scheduler scheduler, ReviewRepository repository, ReviewMapper mapper,
          ServiceUtil serviceUtil) ***REMOVED***
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
    this.scheduler = scheduler;
***REMOVED***

  @Override
  public Review createReview(Review body) ***REMOVED***

    isValidProductId(body.getProductId());

    try ***REMOVED***
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      log.debug(
          "createReview: created a review entity: ***REMOVED******REMOVED***/***REMOVED******REMOVED***", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

***REMOVED*** catch (DataIntegrityViolationException dive) ***REMOVED***
      throw new InvalidInputException(
          "Duplicate key, Product Id: "
              + body.getProductId()
              + ", Review Id:"
              + body.getReviewId());
***REMOVED***
***REMOVED***

  @Override
  public Flux<Review> getReviews(int productId) ***REMOVED***

    isValidProductId(productId);

    return asyncFlux(() -> Flux.fromIterable(getByProductId(productId))).log(null, FINE);
***REMOVED***

  protected List<Review> getByProductId(int productId) ***REMOVED***

    List<Review> list = mapper.entityListToApiList(repository.findByProductId(productId));
    list.forEach(e ->
            e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("getReviews: response size: ***REMOVED******REMOVED***", list.size());

    return list;
***REMOVED***

  @Override
  public void deleteReviews(int productId) ***REMOVED***
    isValidProductId(productId);
    log.debug(
        "deleteReviews: tries to delete reviews for the product with productId: ***REMOVED******REMOVED***", productId);
    repository.deleteAll(repository.findByProductId(productId));
***REMOVED***

  private void isValidProductId(int productId) ***REMOVED***
    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
***REMOVED***

  private <T> Flux<T> asyncFlux(Supplier<Publisher<T>> publisherSupplier) ***REMOVED***
    return Flux.defer(publisherSupplier).subscribeOn(scheduler);
***REMOVED***
***REMOVED***
