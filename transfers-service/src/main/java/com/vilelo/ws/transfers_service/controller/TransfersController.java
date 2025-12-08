package com.vilelo.ws.transfers_service.controller;

import com.vilelo.ws.transfers_service.TransferRestModel;
import com.vilelo.ws.transfers_service.service.TransferService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/transfers")
public class TransfersController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private TransferService transferService;

    public TransfersController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public boolean transfer(@RequestBody TransferRestModel transferRestModel) {
        return transferService.transfer(transferRestModel);
    }



}
