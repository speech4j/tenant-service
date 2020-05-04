package org.speech4j.tenantservice.service.impl;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.repository.general.ConfigRepository;
import org.speech4j.tenantservice.repository.general.UserRepository;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {
    private TenantRepository repository;
    private ConfigRepository configRepository;
    private UserRepository userRepository;

    @Autowired
    public TenantServiceImpl(TenantRepository repository,
                             ConfigRepository configRepository,
                             UserRepository userRepository) {
        this.repository = repository;
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Tenant create(Tenant entity) {
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
        Tenant tenant = findByIdOrThrowException(id);
        tenant.setActive(false);
        configRepository.deleteAllByTenantId(id);
        userRepository.deleteAllByTenantId(id);
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
