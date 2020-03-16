package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.repository.ConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigService extends AbstractService<Config, ConfigRepository> {
    public ConfigService(ConfigRepository configRepository) {
        super(configRepository);
    }
}
