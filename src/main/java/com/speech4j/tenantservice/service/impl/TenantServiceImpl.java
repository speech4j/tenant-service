package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.exception.TenantNotFoundException;
import com.speech4j.tenantservice.repository.TenantRepository;
import com.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {
    private TenantRepository repository;

    @Autowired
    public TenantServiceImpl(TenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tenant create(Tenant entity) {
        //Setting a current date
        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        //Making status active
        entity.setActive(true);

        return repository.save(entity);
    }

    @Override
    public Tenant findById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public Tenant update(Tenant entity, String id) {
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setName(entity.getName());
        return repository.save(tenant);
    }

    @Override
    public void deleteById(String id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<Tenant> findAll() {
        return (List<Tenant>) repository.findAll();
    }

    private Tenant findByIdOrThrowException(String id) {
        //Checking if tenant is found
        return repository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found!"));
    }
}
