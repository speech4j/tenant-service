package org.speech4j.tenantservice.migration.service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Set;

public interface SourceService {
    Set<String> getAllTenants(DataSource dataSource) throws SQLException;
}

