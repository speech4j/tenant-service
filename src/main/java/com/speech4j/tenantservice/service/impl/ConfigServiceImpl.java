package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.exception.ConfigNotFoundException;
import com.speech4j.tenantservice.repository.ConfigRepository;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements EntityService<Config> {
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
        return (List<Config>) repository.findAllByTenantId(id);
    }

    @Override
    public List<Config> findAll() {
        throw new RuntimeException("This method can't be called!");
    }

    private Config findByIdOrThrowException(String id) {
        //Checking if config is found
        return repository.findById(id)
                .orElseThrow(() -> new ConfigNotFoundException("Config not found!"));
    }
}
