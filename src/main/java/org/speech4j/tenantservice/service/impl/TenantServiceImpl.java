package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.mapper.TenantDtoMapper;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TenantServiceImpl implements TenantService {
    private TenantRepository repository;
    private TenantDtoMapper mapper;

    @Autowired
    public TenantServiceImpl(TenantRepository repository,
                             TenantDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Flux<TenantDtoResp> getTenants() {
        return repository.findAll()
                .doOnNext(tenant -> log.debug("TENANT-SERVICE: Tenants were successfully found!"))
                .map(mapper::toDto);
    }

    @Override
    public Mono<TenantDtoResp> create(Tenant entity, String... ids) {
        return repository.save(entity)
                .doOnSuccess(createdTenant -> log.debug("TENANT-SERVICE: Tenant with id:[{}] was successfully created!", createdTenant.getId()))
                .map(mapper::toDto);
    }

    @Override
    public Mono<TenantDtoResp> getById(String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0]).map(mapper::toDto);
    }

    @Override
    public Mono<TenantDtoResp> update(Tenant entity, String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0])
                .flatMap(existingTenant -> {
                    existingTenant.setDescription(entity.getDescription());
                    return repository.save(existingTenant).map(mapper::toDto)
                            .doOnSuccess(success -> log.debug("TENANT-SERVICE: Tenant with id:[{}] was successfully updated!", ids[0]));
                });
    }

    @Override
    public Mono<Void> deleteById(String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0])
                .flatMap(existingCoffee ->
                        repository.delete(existingCoffee).doOnSuccess(existingTenant ->
                                log.debug("TENANT-SERVICE: Tenant with id:[{}] was successfully deleted!", ids[0]))
                );
    }

    private Mono<Tenant> checkIfTenantExistsWithSpecifiedId(String tenantId) {
        return repository.findById(tenantId)
                .switchIfEmpty(
                        Mono.error(new TenantNotFoundException("Tenant by id: [" + tenantId + "] not found!"))
                )
                .onErrorResume(err -> {
                    log.error("TENANT-SERVICE: Tenant by id: [{}] not found!", tenantId);
                    return Mono.error(err);
                })
                .flatMap(existingTenant -> {
                    log.debug("TENANT-SERVICE: Tenant by id: [{}] was successfully found!", tenantId);
                    return Mono.just(existingTenant);
                });
    }
}
