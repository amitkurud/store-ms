package com.siriusxi.ms.store.ps.infra;

import com.siriusxi.ms.store.api.core.product.ProductService;
import com.siriusxi.ms.store.api.core.product.dto.Product;
import com.siriusxi.ms.store.api.event.Event;
import com.siriusxi.ms.store.util.exceptions.EventProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@Log4j2
public class MessageProcessor ***REMOVED***

    private final ProductService productService;

    @Autowired
    public MessageProcessor(@Qualifier("ProductServiceImpl") ProductService productService) ***REMOVED***
        this.productService = productService;
***REMOVED***

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Product> event) ***REMOVED***

        log.info("Process message created at ***REMOVED******REMOVED******REMOVED***", event.getEventCreatedAt());

        switch (event.getEventType()) ***REMOVED***
            case CREATE -> ***REMOVED***
                Product product = event.getData();
                log.info("Create product with ID: ***REMOVED******REMOVED***", product.getProductId());
                productService.createProduct(product);
        ***REMOVED***
            case DELETE -> ***REMOVED***
                log.info("Delete product with Product Id: ***REMOVED******REMOVED***", event.getKey());
                productService.deleteProduct(event.getKey());
        ***REMOVED***
            default -> ***REMOVED***
                String errorMessage =
                        "Incorrect event type: "
                                .concat(event.getEventType().toString())
                                .concat(", expected a CREATE or DELETE event.");
                log.warn(errorMessage);
                throw new EventProcessingException(errorMessage);
        ***REMOVED***
    ***REMOVED***

        log.info("Message processing done!");
***REMOVED***

***REMOVED***
