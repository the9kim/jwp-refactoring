package kitchenpos.support;

import static java.time.LocalDateTime.now;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.support.fixture.MenuFixture.menuRequest;
import static kitchenpos.support.fixture.MenuGroupFixture.단짜_두_마리_메뉴;
import static kitchenpos.support.fixture.ProductFixture.후라이드_치킨_요청_DTO;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.menu.presentation.dto.request.MenuProductRequest;
import kitchenpos.order.presentation.dto.request.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.request.OrderRequest;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
import kitchenpos.order.presentation.dto.request.TableGroupRequest;
import kitchenpos.menu.presentation.dto.response.MenuResponse;
import kitchenpos.order.presentation.dto.response.OrderResponse;
import kitchenpos.order.presentation.dto.response.OrderTableResponse;
import kitchenpos.order.presentation.dto.response.TableGroupResponse;
import kitchenpos.support.fixture.ProductFixture.WrapProductRequest;

public abstract class KitchenPosE2eTest extends E2eTest {

    public static final String PRODUCT_URL = "/api/products";
    public static final String MENU_URL = "/api/menus";
    public static final String MENU_GROUP_URL = "/api/menu-groups";
    public static final String ORDER_URL = "/api/orders";
    public static final String TABLE_URL = "/api/tables";
    public static final String TABLE_GROUP_URL = "/api/table-groups";
    public static final String TABLE_CHANGE_EMPTY_URL = "/api/tables/{orderTableId}/empty";
    public static final String TABLE_GROUP_DELETE_URL = "/api/table-groups/{tableGroupId}";
    public static final String ORDER_CHANGE_ORDER_STATUS_API = "/api/orders/{orderId}/order-status";
    public static final String ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS = "/api/tables/{orderTableId}/number-of-guests";


    protected ExtractableResponse<Response> 주문_생성(final Long 주문테이블_ID, final LocalDateTime 주문일시) {

        final Long 메뉴_ID = 메뉴_생성().getId();

        final OrderRequest 주문 = new OrderRequest(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItemRequest(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문);
    }

    protected OrderResponse 주문_생성후_반환() {

        Long 메뉴_ID = 메뉴_생성().getId();

        Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        LocalDateTime 주문일시 = now().minusMinutes(1);

        OrderRequest 주문 = new OrderRequest(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItemRequest(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(OrderResponse.class);
    }

    protected OrderResponse 주문_생성후_반환(final OrderStatus orderStatus) {

        Long 메뉴_ID = 메뉴_생성().getId();

        Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        LocalDateTime 주문일시 = now().minusMinutes(1);

        OrderRequest 주문 = new OrderRequest(주문테이블_ID, orderStatus.name(), 주문일시, List.of(new OrderLineItemRequest(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(OrderResponse.class);
    }

    protected void 주문들_생성(int size) {

        for (int i = 0; i < size; i++) {
            주문_생성후_반환();
        }
    }

    protected OrderTableResponse 주문테이블_생성_및_변환(OrderTableRequest orderTableRequest) {

        return 주문테이블_생성(orderTableRequest)
                .body()
                .as(OrderTableResponse.class);
    }

    protected ExtractableResponse<Response> 주문테이블_생성(OrderTableRequest orderTableRequest) {

        return POST_요청(TABLE_URL, orderTableRequest);
    }

    protected List<OrderTableRequest> 주문테이블들_생성(int size, OrderTableRequest orderTableRequest) {

        List<OrderTableRequest> orderTablesRequests = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            final OrderTableResponse orderTableResponse = 주문테이블_생성_및_변환(orderTableRequest);
            orderTablesRequests.add(convertToOrderTableRequest(orderTableResponse));
        }

        return orderTablesRequests;
    }

    protected List<OrderTableRequest> 주문테이블들_생성(int size) {

        return 주문테이블들_생성(size, new OrderTableRequest(0, true));
    }

    private OrderTableRequest convertToOrderTableRequest(OrderTableResponse tableResponse) {

        return new OrderTableRequest(
                tableResponse.getId(),
                tableResponse.getTableGroupId(),
                tableResponse.getNumberOfGuests(),
                tableResponse.isEmpty()
        );
    }

    protected TableGroupResponse 테이블그룹_생성(final TableGroupRequest tableGroupRequest) {

        final ExtractableResponse<Response> 응답 = POST_요청(TABLE_GROUP_URL, tableGroupRequest);
        return 응답.body().as(TableGroupResponse.class);
    }

    protected MenuResponse 메뉴_생성() {

        final Long 메뉴그룹_ID = 메뉴그룹_생성후_반환().getId();
        final Product 후라이드_치킨 = 후라이드_치킨_생성후_반환();
        final List<MenuProductRequest> 메뉴상품_리스트 = List.of(new MenuProductRequest(후라이드_치킨.getId(), 1));

        return POST_요청(MENU_URL, menuRequest("단 치킨 한 마리", 15_000, 메뉴그룹_ID, 메뉴상품_리스트))
                .as(MenuResponse.class);
    }

    protected MenuGroup 메뉴_그룹_생성(final MenuGroup menuGroup) {
        return POST_요청(MENU_GROUP_URL, menuGroup).body().as(MenuGroup.class);
    }

    protected Product 상품_생성(WrapProductRequest productRequest) {

        return POST_요청(PRODUCT_URL, productRequest)
                .as(Product.class);
    }

    protected Product 후라이드_치킨_생성후_반환() {

        return POST_요청(PRODUCT_URL, 후라이드_치킨_요청_DTO)
                .as(Product.class);
    }

    protected MenuGroup 메뉴그룹_생성후_반환() {

        return POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴)
                .as(MenuGroup.class);
    }
}