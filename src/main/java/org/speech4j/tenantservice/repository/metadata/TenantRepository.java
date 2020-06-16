package org.speech4j.tenantservice.repository.metadata;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TenantRepository extends ReactiveCrudRepository<Tenant, String> {
    @Query("SELECT * FROM tenants")
    Flux<Tenant> findAll();
//    @Query("INSERT INTO tenants (name) VALUES (:name)")
//    Mono<Tenant> create(String name);

}
