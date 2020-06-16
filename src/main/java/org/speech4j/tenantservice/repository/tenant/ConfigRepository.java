package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.Config;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ConfigRepository extends ReactiveCrudRepository<Config, String> {
    Flux<Config> getAllByTenantId(String tenantId);
}
