package com.speech4j.tenantservice.repository;

import com.speech4j.tenantservice.entity.Config;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends CrudRepository<Config, String> {
    List<Config> findAllByTenantId(String tenantId);

    void deleteAllByTenantId(String tenantId);
}
