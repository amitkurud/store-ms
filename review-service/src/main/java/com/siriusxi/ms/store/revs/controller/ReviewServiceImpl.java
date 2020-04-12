package com.siriusxi.ms.store.revs.controller;

import com.siriusxi.ms.store.api.core.review.Review;
import com.siriusxi.ms.store.api.core.review.ReviewService;
import com.siriusxi.ms.store.revs.persistence.ReviewEntity;
import com.siriusxi.ms.store.revs.persistence.ReviewRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
public class ReviewServiceImpl implements ReviewService ***REMOVED***

  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final ServiceUtil serviceUtil;

  @Autowired
  public ReviewServiceImpl(
      ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) ***REMOVED***
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
***REMOVED***

  @Override
  public Review createReview(Review body) ***REMOVED***
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
  public List<Review> getReviews(int productId) ***REMOVED***

    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("getReviews: response size: ***REMOVED******REMOVED***", list.size());

    return list;
***REMOVED***

  @Override
  public void deleteReviews(int productId) ***REMOVED***
    log.debug(
        "deleteReviews: tries to delete reviews for the product with productId: ***REMOVED******REMOVED***", productId);
    repository.deleteAll(repository.findByProductId(productId));
***REMOVED***
***REMOVED***
