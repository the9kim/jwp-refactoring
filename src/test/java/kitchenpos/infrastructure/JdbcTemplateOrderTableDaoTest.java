package kitchenpos.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class JdbcTemplateOrderTableDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("주문 테이블을 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId());

            OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

            Long actual = savedOrderTable.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("아이디로로 주문 테이블을 단일 조회한다.")
        void success() {
            Long id = orderTable.getId();

            OrderTable actual = jdbcTemplateOrderTableDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("주문 테이블 전체 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

            assertThat(orderTables).hasSize(2);
        }
    }

    @Nested
    @DisplayName("FindAllByIdIn 메서드는")
    class FindAllByIdIn {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable1 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            orderTable2 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_TWO_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("아이디 목록을 받으면 주문 테이블 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));

            assertThat(orderTables).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findAllByTableGroupId 메서드는")
    class FindAllByTableGroupId {

        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_TWO_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("테이블 그룹 아이디를 받으면 포함된 주문 테이블 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId());

            assertThat(orderTables).hasSize(2);
        }
    }
}