package org.speech4j.tenantservice.migration.service;

import java.util.Set;

public interface InitService {
    void initSchema(Set<String> tenants);
}
