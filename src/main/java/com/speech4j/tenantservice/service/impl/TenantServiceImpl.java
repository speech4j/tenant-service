package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.exception.TenantNotFoundException;
import com.speech4j.tenantservice.repository.TenantRepository;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TenantServiceImpl implements EntityService<Tenant> {
    private TenantRepository repository;

    @Autowired
    public TenantServiceImpl(TenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tenant create(Tenant entity) {
        entity.setCreatedDate(ZonedDateTime.now(ZoneId.systemDefault()));
        return repository.save(entity);
    }

    @Override
    public Tenant findById(Long id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public Tenant update(Tenant entity) {
        Tenant tenant = findByIdOrThrowException(entity.getId());
        tenant.setName(entity.getName());
        return repository.save(tenant);
    }

    @Override
    public void deleteById(Long id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<Tenant> findAll() {
        return (List<Tenant>) repository.findAll();
    }

    private Tenant findByIdOrThrowException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found!"));
    }
}
