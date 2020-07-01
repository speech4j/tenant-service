package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.ApiName;
import org.speech4j.tenantservice.entity.tenant.Config;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConfigRepository extends ReactiveCrudRepository<Config, String> {

    @Query("INSERT INTO configs (id, apiname, credentials, tenantid) " +
            "VALUES (:id, :apiName, :credentials, :tenantId)")
    Mono<Config> create(String id, ApiName apiName, String credentials, String tenantId);

    Flux<Config> getAllByTenantId(String tenantId);
}
