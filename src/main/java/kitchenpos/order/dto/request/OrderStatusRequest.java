package kitchenpos.order.dto.request;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {

    private OrderStatus orderStatus;

    private OrderStatusRequest() {
    }

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}