package kitchenpos.order.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;

public interface OrderDao {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Order update(Order savedOrder);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}