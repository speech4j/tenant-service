package org.speech4j.tenantservice.migration.service;

import java.util.List;

public interface InitService {
    void initSchema(List<String> tenants);
}
