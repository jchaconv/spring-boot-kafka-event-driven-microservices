package com.vilelo.ws.products_microservice.service;

import com.vilelo.ws.core.ProductCreatedEvent;
import com.vilelo.ws.products_microservice.rest.CreateProductRestModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER =  LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {

        String productId= UUID.randomUUID().toString();

        //TODO: Persist Product Details into database table before publishing an Event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(),
                productRestModel.getPrice(), productRestModel.getQuantity());

        LOGGER.info("Before publishing a ProductCreatedEvent");

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get(); //sync

        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("Offset: " + result.getRecordMetadata().offset());

        LOGGER.info("*********** Returning product id");

        return productId;
    }
}
