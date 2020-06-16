package org.speech4j.tenantservice.service;

import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.tenant.Config;
import reactor.core.publisher.Flux;

public interface ConfigService extends EntityService<Config, ConfigDtoResp> {
    Flux<ConfigDtoResp> getAllById(String tenantId);
}
