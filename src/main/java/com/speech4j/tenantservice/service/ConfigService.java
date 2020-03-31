package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.Config;

import java.util.List;

public interface ConfigService extends EntityService<Config> {
    List<Config> findAllById(String id);
}
