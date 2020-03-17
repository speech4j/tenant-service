package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController implements EntityController<Tenant>{
    private EntityService<Tenant> service;

    @Autowired
    public TenantController(EntityService<Tenant> service) {
        this.service = service;
    }

    @Override
    public Tenant save(Tenant entity) {
        return service.create(entity);
    }

    @Override
    public Tenant findById(Long id) {
        return service.findById(id);
    }

    @Override
    public Tenant update(Tenant entity) {
        return service.update(entity);
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<Tenant> findAll() {
        return service.findAll();
    }
}
