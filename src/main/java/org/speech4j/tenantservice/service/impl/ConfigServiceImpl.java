package org.speech4j.tenantservice.service.impl;

import org.speech4j.tenantservice.entity.general.Config;
import org.speech4j.tenantservice.exception.ConfigNotFoundException;
import org.speech4j.tenantservice.repository.general.ConfigRepository;
import org.speech4j.tenantservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    private ConfigRepository repository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public Config create(Config entity) {
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
