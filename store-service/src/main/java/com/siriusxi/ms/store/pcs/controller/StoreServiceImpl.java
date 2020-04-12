package com.siriusxi.ms.store.pcs.controller;

import com.siriusxi.ms.store.api.composite.StoreService;
import com.siriusxi.ms.store.api.composite.dto.ProductAggregate;
import com.siriusxi.ms.store.api.composite.dto.RecommendationSummary;
import com.siriusxi.ms.store.api.composite.dto.ReviewSummary;
import com.siriusxi.ms.store.api.composite.dto.ServiceAddresses;
import com.siriusxi.ms.store.api.core.product.Product;
import com.siriusxi.ms.store.api.core.recommendation.Recommendation;
import com.siriusxi.ms.store.api.core.review.Review;
import com.siriusxi.ms.store.pcs.integration.StoreIntegration;
import com.siriusxi.ms.store.util.exceptions.NotFoundException;
import com.siriusxi.ms.store.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class StoreServiceImpl implements StoreService ***REMOVED***

    private final ServiceUtil serviceUtil;
    private final StoreIntegration integration;

    @Autowired
    public StoreServiceImpl(ServiceUtil serviceUtil,
                            StoreIntegration integration) ***REMOVED***
        this.serviceUtil = serviceUtil;
        this.integration = integration;
***REMOVED***

    @Override
    public void createProduct(ProductAggregate body) ***REMOVED***

        try ***REMOVED***

            log.debug("createCompositeProduct: creates a new composite entity for productId: ***REMOVED******REMOVED***",
                    body.getProductId());

            Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
            integration.createProduct(product);

            if (body.getRecommendations() != null) ***REMOVED***
                body.getRecommendations().forEach(r -> ***REMOVED***
                    Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent(), null);
                    integration.createRecommendation(recommendation);
            ***REMOVED***);
        ***REMOVED***

            if (body.getReviews() != null) ***REMOVED***
                body.getReviews().forEach(r -> ***REMOVED***
                    Review review = new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
                    integration.createReview(review);
            ***REMOVED***);
        ***REMOVED***

            log.debug("createCompositeProduct: composite entites created for productId: ***REMOVED******REMOVED***",
                    body.getProductId());

    ***REMOVED*** catch (RuntimeException re) ***REMOVED***
            log.warn("createCompositeProduct failed", re);
            throw re;
    ***REMOVED***
***REMOVED***

    @Override
    public ProductAggregate getProduct(int productId) ***REMOVED***
        log.debug("getCompositeProduct: lookup a product aggregate for productId: ***REMOVED******REMOVED***", productId);

        Product product = integration.getProduct(productId);
        if (product == null) throw new NotFoundException("No product found for productId: " + productId);

        List<Recommendation> recommendations = integration.getRecommendations(productId);

        List<Review> reviews = integration.getReviews(productId);

        log.debug("getCompositeProduct: aggregate entity found for productId: ***REMOVED******REMOVED***", productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
***REMOVED***

    @Override
    public void deleteProduct(int productId) ***REMOVED***

        log.debug("deleteCompositeProduct: Deletes a product aggregate for productId: ***REMOVED******REMOVED***",
                productId);

        integration.deleteProduct(productId);

        integration.deleteRecommendations(productId);

        integration.deleteReviews(productId);

        log.debug("getCompositeProduct: aggregate entities deleted for productId: ***REMOVED******REMOVED***", productId);
***REMOVED***

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) ***REMOVED***

        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null :
                recommendations.stream()
                        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
                        .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null)  ? null :
                reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
                        .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices addresses
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
***REMOVED***
***REMOVED***
