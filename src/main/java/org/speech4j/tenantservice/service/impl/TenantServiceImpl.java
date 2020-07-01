package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.exception.DuplicateEntityException;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.mapper.TenantDtoMapper;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public Mono<TenantDtoResp> create(Tenant tenant, String... ids) {
        LocalDate createdDate = LocalDateTime.now().toLocalDate();
        tenant.setCreatedDate(createdDate);
        tenant.setModifiedDate(createdDate);

        return repository.create(
                tenant.getId(),
                tenant.getDescription(),
                tenant.getCreatedDate(),
                tenant.getModifiedDate(),
                tenant.isActive()
        ).onErrorResume(err -> {
            log.error("TENANT-SERVICE: Tenant with a specified email: [{}] already exists!", tenant.getId());
            return Mono.error(
                    new DuplicateEntityException("Tenant with a specified id:[" + tenant.getId() + "] already exists!"));
        }).thenReturn(tenant).map(createdTenant -> {
            log.debug("TENANT-SERVICE: Tenant with a specified id: [{}] was successfully created!", tenant.getId());
            return mapper.toDto(createdTenant);
        });
    }

    @Override
    public Mono<TenantDtoResp> getById(String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0]).map(mapper::toDto);
    }

    @Override
    public Mono<TenantDtoResp> update(Tenant tenant, String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0])
                .flatMap(existingTenant -> {
                    existingTenant.setDescription(tenant.getDescription());
                    existingTenant.setModifiedDate(LocalDateTime.now().toLocalDate());
                    return repository.save(existingTenant).map(mapper::toDto)
                            .doOnSuccess(success -> log.debug("TENANT-SERVICE: Tenant by a specified id:[{}] was successfully updated!", ids[0]));
                });
    }

    @Override
    public Mono<Void> deleteById(String... ids) {
        return checkIfTenantExistsWithSpecifiedId(ids[0])
                .flatMap(existingTenant ->
                        repository.deactivate(existingTenant.getId()).doOnSuccess(response ->
                                log.debug("TENANT-SERVICE: Tenant by a specified id:[{}] was successfully deleted!", ids[0]))
                );
    }

    private Mono<Tenant> checkIfTenantExistsWithSpecifiedId(String tenantId) {
        return repository.findById(tenantId).filter(Tenant::isActive)
                .switchIfEmpty(
                        Mono.error(new TenantNotFoundException("Tenant by a specified id: [" + tenantId + "] not found!"))
                )
                .onErrorResume(err -> {
                    log.error("TENANT-SERVICE: Tenant by a specified id: [{}] not found!", tenantId);
                    return Mono.error(err);
                })
                .flatMap(existingTenant -> {
                    log.debug("TENANT-SERVICE: Tenant by a specified id: [{}] was successfully found!", tenantId);
                    return Mono.defer(()->Mono.just(existingTenant));
                });
    }
}
