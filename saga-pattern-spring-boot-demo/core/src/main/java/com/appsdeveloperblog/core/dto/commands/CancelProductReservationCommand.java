package com.appsdeveloperblog.core.dto.commands;

import java.util.UUID;

public class CancelProductReservationCommand {

    private UUID productId;
    private UUID orderId;
    private Integer quantity;

    public CancelProductReservationCommand() {
    }

    public CancelProductReservationCommand(UUID productId, UUID orderId, Integer quantity) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
