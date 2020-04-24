package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.general.Config;
import com.speech4j.tenantservice.exception.ConfigNotFoundException;
import com.speech4j.tenantservice.liquibase.service.LiquibaseService;
import com.speech4j.tenantservice.repository.ConfigRepository;
import com.speech4j.tenantservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    private ConfigRepository repository;
    private LiquibaseService liquibaseService;

    @Autowired
    public ConfigServiceImpl(ConfigRepository repository,
                             LiquibaseService liquibaseService) {
        this.repository = repository;
        this.liquibaseService = liquibaseService;
    }

    @Override
    public Config create(Config entity) {
            liquibaseService.run("tenant100");
            return repository.save(entity);
    }

    @Override
    public Config findById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public Config update(Config entity, String id) {
        findByIdOrThrowException(id);
        return repository.save(entity);
    }

    @Override
    public void deleteById(String id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<Config> findAllById(String id) {
        List<Config> list = repository.findAllByTenantId(id);
        if (!list.isEmpty()){
            return list;
        }else {
            throw new ConfigNotFoundException("Config not found!");
        }
    }

    private Config findByIdOrThrowException(String id) {
        //Checking if config is found
        return repository.findById(id)
                .orElseThrow(() -> new ConfigNotFoundException("Config not found!"));
    }
}
