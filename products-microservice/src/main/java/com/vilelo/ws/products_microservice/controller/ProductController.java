package com.vilelo.ws.products_microservice.controller;

import com.vilelo.ws.products_microservice.rest.CreateProductRestModel;
import com.vilelo.ws.products_microservice.rest.ErrorMessage;
import com.vilelo.ws.products_microservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    final ProductService productService;
    private final Logger LOGGER =  LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRestModel product) {
        String productId;
        try {
            productId = productService.createProduct(product);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(), "/products"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }


}
