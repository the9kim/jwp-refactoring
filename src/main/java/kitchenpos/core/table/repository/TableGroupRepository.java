package kitchenpos.core.table.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.domain.TableGroup;
import kitchenpos.core.table.domain.TableGroupDao;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final OrderTableRepository orderTableDao;

    public TableGroupRepository(final JdbcTemplateTableGroupDao tableGroupDao,
                                final OrderTableRepository orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final LinkedList<OrderTable> savedOrderTables = new LinkedList<>();
        for (final OrderTable orderTable : entity.getOrderTables()) {
            savedOrderTables.add(orderTableDao.save(orderTable));
        }
        final TableGroup tableGroup = tableGroupDao.save(entity);
        tableGroup.changeOrderTables(savedOrderTables);
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroupDao.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        final List<TableGroup> tableGroups = tableGroupDao.findAll();
        for (final TableGroup tableGroup : tableGroups) {
            tableGroup.changeOrderTables(orderTableDao.findAllByTableGroupId(tableGroup.getId()));
        }
        return tableGroups;
    }
}