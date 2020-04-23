package com.siriusxi.ms.store.revs.infra;

import com.siriusxi.ms.store.api.core.review.ReviewService;
import com.siriusxi.ms.store.api.core.review.dto.Review;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.util.exceptions.EventProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import static java.lang.String.valueOf;

@EnableBinding(Sink.class)
@Log4j2
public class MessageProcessor ***REMOVED***

    private final ReviewService service;

    @Autowired
    public MessageProcessor(@Qualifier("ReviewServiceImpl") ReviewService service) ***REMOVED***
        this.service = service;
***REMOVED***

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Review> event) ***REMOVED***

        log.info("Process message created at ***REMOVED******REMOVED******REMOVED***", event.getEventCreatedAt());

        switch (event.getEventType()) ***REMOVED***
            case CREATE -> ***REMOVED***
                Review review = event.getData();
                log.info("Create review with ID: ***REMOVED******REMOVED***/***REMOVED******REMOVED***", review.getProductId(),
                        review.getReviewId());
                service.createReview(review);
        ***REMOVED***
            case DELETE -> ***REMOVED***
                int productId = event.getKey();
                log.info("Delete review with Product Id: ***REMOVED******REMOVED***", productId);
                service.deleteReviews(productId);
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