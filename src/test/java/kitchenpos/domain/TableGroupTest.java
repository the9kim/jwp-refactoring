package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Nested
    class validateOrderTableGrouping_메소드는 {

        @Test
        void 테이블이_모두_비어_있지_않다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, 7, false);
            OrderTable orderTable2 = new OrderTable(null, 7, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroup.validateOrderTableGrouping(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_이미_다른_그룹에_속해있다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, 7, false);
            OrderTable orderTable2 = new OrderTable(1L, 7, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroup.validateOrderTableGrouping(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_없다면_예외가_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroup.validateOrderTableGrouping(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_2개_이하라면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, 7, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroup.validateOrderTableGrouping(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력_받은_값과_테이블의_사이즈가_다르다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, 7, false);
            OrderTable orderTable2 = new OrderTable(null, 7, false);
            OrderTable orderTable3 = new OrderTable(null, 7, false);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2, orderTable3));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroup.validateOrderTableGrouping(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}