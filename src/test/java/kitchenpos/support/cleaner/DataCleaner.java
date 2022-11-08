package kitchenpos.support.cleaner;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleaner implements InitializingBean {

    private static final String TRUNCATE_FORMAT = "TRUNCATE TABLE %s";
    private static final String REFERENTIAL_FORMAT = "SET REFERENTIAL_INTEGRITY %s";

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    @Transactional
    public void clear() {
        entityManager.clear();
        truncate();
    }

    private void truncate() {
        entityManager.createNativeQuery(String.format(REFERENTIAL_FORMAT, "FALSE")).executeUpdate();
        tableNames.stream()
            .filter(tableName -> !tableName.contains("flyway_schema_history"))
            .forEach(tableName -> entityManager.createNativeQuery(String.format(TRUNCATE_FORMAT, tableName)).executeUpdate());
        entityManager.createNativeQuery(String.format(REFERENTIAL_FORMAT, "TRUE")).executeUpdate();
    }

    @Override
    public void afterPropertiesSet() {
        tableNames = new ArrayList<>();
        try {
            final DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            final ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
