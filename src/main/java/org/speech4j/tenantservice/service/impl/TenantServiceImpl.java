package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.exception.DuplicateEntityException;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.migration.service.InitService;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
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
        try {
            findByIdOrThrowException(entity.getId());
            throw new DuplicateEntityException("Tenant with a specified id already exists!");
        }catch (TenantNotFoundException e) {
            Tenant tenant = repository.save(entity);
            initService.initSchema(Arrays.asList(tenant.getId()));
            log.debug("TENANT-SERVICE: Tenant with [ id: {}] was successfully created!", entity.getId());
            return tenant;
        }
    }

    @Override
    public Tenant findById(String id) {
        Tenant tenant = findByIdOrThrowException(id);
        log.debug("TENANT-SERVICE: Tenant with [ id: {}] was successfully found!", id);
        return tenant;
    }

    @Override
    public Tenant update(Tenant entity, String id) {
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setDescription(entity.getDescription());

        Tenant updatedTenant =  repository.save(tenant);
        log.debug("TENANT-SERVICE: Tenant with [ id: {}] was successfully updated!", id);
        return updatedTenant;
    }

    @Override
    public void deleteById(String id) {
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setActive(false);
        repository.save(tenant);
        log.debug("TENANT-SERVICE: Tenant with [ id: {}] was successfully deleted!", id);
    }

    @Override
    public List<Tenant> findAll() {
        List<Tenant> tenats = (List<Tenant>) repository.findAll();
        log.debug("TENANT-SERVICE: Tenants with was successfully found!");
        return tenats;
    }

    private Tenant findByIdOrThrowException(String id) {
        //Checking if tenant is found
        return repository.findById(id).filter(Tenant::isActive)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found!"));
    }
}
