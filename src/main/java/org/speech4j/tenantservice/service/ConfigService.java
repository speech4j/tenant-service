package org.speech4j.tenantservice.service;

import org.speech4j.tenantservice.entity.general.Config;

import java.util.List;

public interface ConfigService extends EntityService<Config> {
    List<Config> findAllById(String id);
}
