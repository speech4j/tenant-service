package org.speech4j.tenantservice.service;

import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import reactor.core.publisher.Flux;

public interface TenantService extends EntityService<Tenant, TenantDtoResp>{
    Flux<TenantDtoResp> getTenants();
}
