package com.siriusxi.ms.store.rs.controller;

import com.siriusxi.ms.store.api.core.recommendation.Recommendation;
import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.util.exceptions.InvalidInputException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class RecommendationServiceImpl implements RecommendationService ***REMOVED***

    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil) ***REMOVED***
        this.serviceUtil = serviceUtil;
***REMOVED***

    @Override
    public List<Recommendation> getRecommendations(int productId) ***REMOVED***

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        if (productId == 113) ***REMOVED***
            log.debug("No recommendations found for productId: ***REMOVED******REMOVED***", productId);
            return new ArrayList<>();
    ***REMOVED***

        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 3, "Author 3", 3, "Content 3", serviceUtil.getServiceAddress()));

        log.debug("/recommendation response size: ***REMOVED******REMOVED***", list.size());

        return list;
***REMOVED***
***REMOVED***
