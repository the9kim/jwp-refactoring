package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.repository.Repository;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {
    OrderLineItem save(OrderLineItem entity);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
