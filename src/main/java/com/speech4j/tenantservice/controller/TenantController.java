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

@RestController
@RequestMapping("/tenants")
public class TenantController {

    private EntityService<Tenant> service;

    @Autowired
    public TenantController(EntityService<Tenant> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Tenant findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }
}
