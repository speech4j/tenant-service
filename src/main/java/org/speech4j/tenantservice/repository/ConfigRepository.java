package org.speech4j.tenantservice.repository;

import org.speech4j.tenantservice.entity.general.Config;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigRepository extends CrudRepository<Config, String> {
    List<Config> findAllByTenantId(String tenantId);

    void deleteAllByTenantId(String tenantId);
}
