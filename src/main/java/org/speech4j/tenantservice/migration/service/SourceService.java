package org.speech4j.tenantservice.migration.service;

import java.util.List;

public interface SourceService {
    void insertDefaultData();
    List<String> getAllTenantIdentifiers();
}

