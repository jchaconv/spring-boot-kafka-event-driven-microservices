package com.vilelo.ws.transfers_service.error;

public class TransferServiceException extends RuntimeException {

    public TransferServiceException(Throwable cause) {
        super(cause);
    }

    public TransferServiceException(String message) {
        super(message);
    }

}
