package org.speech4j.tenantservice.migration.service;

import java.util.List;

public interface MetadataService {
    void insertDefaultTenant();
    List<String> getAllTenantIdentifiers();
}

