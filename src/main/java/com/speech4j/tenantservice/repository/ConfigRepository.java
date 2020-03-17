package com.speech4j.tenantservice.repository;

import com.speech4j.tenantservice.entity.Config;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Long> {
}
