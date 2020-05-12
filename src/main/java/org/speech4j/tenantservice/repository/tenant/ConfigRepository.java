package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.Config;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigRepository extends CrudRepository<Config, String> {
    List<Config> findAllByTenantId(String tenantId);
}
