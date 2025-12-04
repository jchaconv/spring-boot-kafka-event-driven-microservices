package com.vilelo.ws.products_microservice.service;

import com.vilelo.ws.products_microservice.rest.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel) throws Exception;

}
