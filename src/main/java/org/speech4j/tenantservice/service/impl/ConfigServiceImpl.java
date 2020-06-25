package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.tenant.Config;
import org.speech4j.tenantservice.exception.ConfigNotFoundException;
import org.speech4j.tenantservice.mapper.ConfigDtoMapper;
import org.speech4j.tenantservice.repository.tenant.ConfigRepository;
import org.speech4j.tenantservice.service.ConfigService;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.TENANT_KEY;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    private ConfigRepository repository;
    private TenantService tenantService;
    private ConfigDtoMapper mapper;

    @Autowired
    public ConfigServiceImpl(ConfigRepository repository,
                             TenantService tenantService,
                             ConfigDtoMapper mapper) {
        this.repository = repository;
        this.tenantService = tenantService;
        this.mapper = mapper;
    }

    @Override
    public Mono<ConfigDtoResp> create(Config config, String... ids) {
        config.setTenantId(ids[0]);
        config.setId(UUID.randomUUID().toString());
        return repository.create(
                config.getId(), config.getApiName(), config.getCredentials(), config.getTenantId()
        ).subscriberContext(Context.of(TENANT_KEY, ids[0]))

                .thenReturn(config).map(createdConfig -> {
                    log.debug("CONFIG-SERVICE: Config with [ id: {}] was successfully created!", createdConfig.getId());

                    return mapper.toDto(createdConfig);
                });

    }

    @Override
    public Mono<ConfigDtoResp> getById(String... ids) {
        return checkIfExistConfigWithSpecifiedTenantId(ids[0], ids[1]).map(mapper::toDto);
    }

    @Override
    public Mono<ConfigDtoResp> update(Config entity, String... ids) {
        return checkIfExistConfigWithSpecifiedTenantId(ids[0], ids[1])
                .flatMap(existingConfig -> {
                    existingConfig.setCredentials(entity.getCredentials());
                    existingConfig.setApiName(entity.getApiName());
                    return repository.save(existingConfig).map(mapper::toDto)
                            .doOnSuccess(updatedConfig ->
                                    log.debug(
                                            "CONFIG-SERVICE: Config by a specified id:[{}] successfully updated!",
                                            ids[1]
                                    ));
                });
    }

    @Override
    public Mono<Void> deleteById(String... ids) {
        return repository.findById(ids[1])
                .subscriberContext(Context.of(TENANT_KEY, ids[0]))
                .flatMap(existingConfig ->
                        repository.delete(existingConfig).doOnSuccess(success ->
                                log.debug("CONFIG-SERVICE: Config by a specified id:[{}] successfully deleted!", ids[1]))
                );
    }

    @Override
    public Flux<ConfigDtoResp> getAllById(String tenantId) {
        return repository.getAllByTenantId(tenantId)
                .subscriberContext(Context.of(TENANT_KEY, tenantId))
                .switchIfEmpty(
                        Mono.error(new ConfigNotFoundException("Configs by the tenant id: [" + tenantId + "] were not found!"))
                ).onErrorResume(err -> {
                    log.error("CONFIG-SERVICE: Configs by the tenant id: [{}] were not found!", tenantId);
                    return Mono.error(err);
                })
                .doOnNext(existingConfig ->
                        log.debug("CONFIG-SERVICE: Configs by the tenant id: [{}] were successfully found!", tenantId))
                .map(mapper::toDto);
    }

    private Mono<Config> checkIfExistConfigWithSpecifiedTenantId(String tenantId, String configId) {
        return repository.findById(configId)
                .subscriberContext(Context.of(TENANT_KEY, tenantId))
                .switchIfEmpty(
                        Mono.error(new ConfigNotFoundException("Config by a specified id: [" + configId + "] not found!"))
                )
                .onErrorResume(err -> {
                    log.error("CONFIG-SERVICE: Config by a specified id: [{}] not found!", configId);
                    return Mono.error(err);
                })
                .flatMap(existingConfig -> {
                    if (!existingConfig.getTenantId().equals(tenantId)) {
                        log.error("CONFIG-SERVICE: Config by a specified id: [{}] doesn't relate to a tenant id: [{}]!", configId, tenantId);
                        return Mono.error(new ConfigNotFoundException("Config by a specified id: [" + configId + "] doesn't relate to a tenant id: [" + tenantId + "]!"));
                    }
                    log.debug("CONFIG-SERVICE: Config by a specified id: [{}] and a tenant id: [{}] successfully found!", configId, tenantId);
                    return Mono.just(existingConfig);
                });
    }
}
