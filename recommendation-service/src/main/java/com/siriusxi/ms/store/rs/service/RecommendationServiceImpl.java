package com.siriusxi.ms.store.rs.service;

import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.rs.persistence.RecommendationRepository;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    isValidProductId(body.getProductId());

    return repository
            .save(mapper.apiToEntity(body))
            .log()
            .onErrorMap(
                    DuplicateKeyException.class,
                    ex -> new InvalidInputException("Duplicate key, Product Id: "
                            + body.getProductId() + ", Recommendation Id:"
                            + body.getRecommendationId()))
            .map(mapper::entityToApi).block();
***REMOVED***

  @Override
  public Flux<Recommendation> getRecommendations(int productId) ***REMOVED***

    isValidProductId(productId);

    return repository
            .findByProductId(productId)
            .log()
            .map(mapper::entityToApi)
            .map(e -> ***REMOVED***
              e.setServiceAddress(serviceUtil.getServiceAddress());
              return e;
        ***REMOVED***);

      //FIXME check how to add log to flux
    //log.debug("getRecommendations: response size: ***REMOVED******REMOVED***", list.size());
***REMOVED***

  @Override
  public void deleteRecommendations(int productId) ***REMOVED***
    isValidProductId(productId);

    log.debug(
        """ 
           deleteRecommendations: tries to delete recommendations 
           for the product with productId: ***REMOVED******REMOVED***
           """,
        productId);

    repository
            .deleteAll(repository.findByProductId(productId))
            .block();
***REMOVED***

  private void isValidProductId(int productId) ***REMOVED***
    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
***REMOVED***
***REMOVED***
