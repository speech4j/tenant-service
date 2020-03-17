package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configs")
public class ConfigController implements EntityController<Config> {
    private EntityService<Config> service;

    @Autowired
    public ConfigController(EntityService<Config> service) {
        this.service = service;
    }

    @Override
    public Config save(Config entity) {
        return service.create(entity);
    }

    @Override
    public Config findById(Long id) {
        return service.findById(id);
    }

    @Override
    public Config update(Config entity) {
        return service.update(entity);
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<Config> findAll() {
        return service.findAll();
    }
}
