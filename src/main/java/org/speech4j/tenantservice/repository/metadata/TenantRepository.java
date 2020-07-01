package org.speech4j.tenantservice.repository.metadata;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface TenantRepository extends ReactiveCrudRepository<Tenant, String> {

    @Query("INSERT INTO tenants (id, description, createddate, modifieddate, active) " +
            "VALUES (:id,:description, :createdDate,  :modifiedDate, :active)")
    Mono<Tenant> create(String id, String description, LocalDate createdDate, LocalDate modifiedDate , Boolean active);

    @Query(value = "UPDATE tenants SET active = 'false' WHERE id = :id")
    Mono<Void> deactivate(String id);
}