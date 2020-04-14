package com.siriusxi.ms.store.rs.service;

import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.rs.persistence.RecommendationEntity;
import com.siriusxi.ms.store.rs.persistence.RecommendationRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RecommendationServiceImpl")
@Log4j2
public class RecommendationServiceImpl implements RecommendationService ***REMOVED***

  private final RecommendationRepository repository;

  private final RecommendationMapper mapper;

  private final ServiceUtil serviceUtil;

  @Autowired
  public RecommendationServiceImpl(
      RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) ***REMOVED***
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
***REMOVED***

  @Override
  public Recommendation createRecommendation(Recommendation body) ***REMOVED***
    try ***REMOVED***
      RecommendationEntity entity = mapper.apiToEntity(body);
      RecommendationEntity newEntity = repository.save(entity);

      log.debug(
          "createRecommendation: created a recommendation entity: ***REMOVED******REMOVED***/***REMOVED******REMOVED***",
          body.getProductId(),
          body.getRecommendationId());

      return mapper.entityToApi(newEntity);

***REMOVED*** catch (DuplicateKeyException dke) ***REMOVED***
      throw new InvalidInputException(
          "Duplicate key, Product Id: "
              + body.getProductId()
              + ", Recommendation Id:"
              + body.getRecommendationId());
***REMOVED***
***REMOVED***

  @Override
  public List<Recommendation> getRecommendations(int productId) ***REMOVED***

    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

    List<RecommendationEntity> entityList = repository.findByProductId(productId);
    List<Recommendation> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("getRecommendations: response size: ***REMOVED******REMOVED***", list.size());

    return list;
***REMOVED***

  @Override
  public void deleteRecommendations(int productId) ***REMOVED***
    log.debug(
        "deleteRecommendations: tries to delete recommendations for the product with "
            + "productId: ***REMOVED******REMOVED***",
        productId);
    repository.deleteAll(repository.findByProductId(productId));
***REMOVED***
***REMOVED***
