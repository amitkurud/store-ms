package com.siriusxi.ms.store.rs.infra;

import com.siriusxi.ms.store.api.core.recommendation.RecommendationService;
import com.siriusxi.ms.store.api.core.recommendation.dto.Recommendation;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.util.exceptions.EventProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import static java.lang.String.*;

@EnableBinding(Sink.class)
@Log4j2
public class MessageProcessor ***REMOVED***

    private final RecommendationService service;

    @Autowired
    public MessageProcessor(@Qualifier("RecommendationServiceImpl") RecommendationService service) ***REMOVED***
        this.service = service;
***REMOVED***

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Recommendation> event) ***REMOVED***

        log.info("Process message created at ***REMOVED******REMOVED******REMOVED***", event.getEventCreatedAt());

        switch (event.getEventType()) ***REMOVED***
            case CREATE -> ***REMOVED***
                Recommendation recommendation = event.getData();
                log.info("Create recommendation with ID: ***REMOVED******REMOVED***/***REMOVED******REMOVED***", recommendation.getProductId(),
                        recommendation.getRecommendationId());
                service.createRecommendation(recommendation);
        ***REMOVED***
            case DELETE -> ***REMOVED***
                int productId = event.getKey();
                log.info("Delete recommendations with ProductID: ***REMOVED******REMOVED***", productId);
                service.deleteRecommendations(productId);
        ***REMOVED***
            default -> ***REMOVED***
                String errorMessage =
                        "Incorrect event type: "
                                .concat(valueOf(event.getEventType()))
                                .concat(", expected a CREATE or DELETE event");
                log.warn(errorMessage);
                throw new EventProcessingException(errorMessage);
        ***REMOVED***
    ***REMOVED***

        log.info("Message processing done!");
***REMOVED***

***REMOVED***
