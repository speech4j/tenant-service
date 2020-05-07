package org.speech4j.tenantservice.service.impl;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.migration.service.InitService;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {
    private TenantRepository repository;
    private InitService initService;

    @Autowired
    public TenantServiceImpl(TenantRepository repository,
                             InitService initService) {
        this.repository = repository;
        this.initService = initService;
    }

    @Override
    public Tenant create(Tenant entity) {
        Tenant tenant = repository.save(entity);
        initService.initSchema(Arrays.asList(tenant.getId()));
        return tenant;
    }

    @Override
    public Tenant findById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public Tenant update(Tenant entity, String id) {
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setDescription(entity.getDescription());
        return repository.save(tenant);
    }

    @Override
    public void deleteById(String id) {
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setActive(false);
        repository.save(tenant);
    }

    @Override
    public List<Tenant> findAll() {
        return (List<Tenant>) repository.findAll();
    }

    private Tenant findByIdOrThrowException(String id) {
        //Checking if tenant is found
        return repository.findById(id).filter(Tenant::isActive)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found!"));
    }
}
