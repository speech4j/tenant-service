package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.entity.general.Config;
import org.speech4j.tenantservice.exception.ConfigNotFoundException;
import org.speech4j.tenantservice.repository.general.ConfigRepository;
import org.speech4j.tenantservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    private ConfigRepository repository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public Config create(Config entity) {
            Config config = repository.save(entity);
            log.debug("CONFIG-SERVICE: Config with [ id: {}] was successfully created!", entity.getId());
            return config;
    }

    @Override
    public Config findById(String id) {
        Config config = findByIdOrThrowException(id);
        log.debug("CONFIG-SERVICE: Config with [ id: {}] was successfully found!", id);
        return config;
    }

    @Override
    public Config update(Config entity, String id) {
        findByIdOrThrowException(id);
        Config config = repository.save(entity);
        log.debug("CONFIG-SERVICE: Config with [ id: {}] was successfully updated!", id);
        return config;
    }

    @Override
    public void deleteById(String id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
        log.debug("CONFIG-SERVICE: Config with [ id: {}] was successfully deleted!", id);
    }

    @Override
    public List<Config> findAllById(String id) {
        List<Config> list = repository.findAllByTenantId(id);
        if (!list.isEmpty()){
            log.debug("CONFIG-SERVICE: Configs with [ tenantId: {}] were successfully found!", id);
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
