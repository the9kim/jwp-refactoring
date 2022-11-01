package kitchenpos.application.dto;

import java.util.List;

public class TableGroupRequest {

    private List<TableGroupOrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
