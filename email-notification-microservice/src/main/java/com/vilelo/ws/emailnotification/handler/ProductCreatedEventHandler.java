package com.vilelo.ws.emailnotification.handler;

import com.vilelo.ws.core.ProductCreatedEvent;
import com.vilelo.ws.emailnotification.error.NotRetryableException;
import com.vilelo.ws.emailnotification.error.RetryableException;
import com.vilelo.ws.emailnotification.io.ProcessedEventEntity;
import com.vilelo.ws.emailnotification.io.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductCreatedEventHandler.class);
    private RestTemplate restTemplate;
    private ProcessedEventRepository processedEventRepository;

    public ProductCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepository processedEventRepository) {
        this.restTemplate = restTemplate;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    @KafkaHandler
    public void handle(@Payload ProductCreatedEvent productCreatedEvent,
                       @Header("messageId") String messageId, //add required=true if it is mandatory
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        //if(true) throw new NotRetryableException("An error took place. No need to consume this message again.");
        LOGGER.info("Received a new event: {} with productId: {}",
                productCreatedEvent.getTitle(), productCreatedEvent.getProductId());

        //Check if the message was already processed before
        ProcessedEventEntity existingRecord = processedEventRepository.findByMessageId(messageId);
        if (existingRecord != null) {
            LOGGER.info("The event with id: {} has already been processed", existingRecord.getMessageId());
            return;
        }

        String requestUrl = "http://localhost:8085/response/200"; //change to 500 to test error

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

            if(response.getStatusCode().value() == HttpStatus.OK.value()) {
                LOGGER.info("Received response from a remote service: {}", response.getBody());
            }
        } catch (ResourceAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw new RetryableException(ex);
        } catch (HttpServerErrorException ex) {
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

        //Save a unique message id in the database table
        try {
            processedEventRepository.save(new ProcessedEventEntity(messageId,
                    productCreatedEvent.getProductId()));
        } catch (DataIntegrityViolationException ex) {
            throw new NotRetryableException(ex);
        }

    }




}
