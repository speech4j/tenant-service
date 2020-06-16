package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    @Query(value = "update User u set u.active = 'false' where u.id = :id")
    Mono<Void> deactivate(String id);
    Flux<User> getAllByTenantId(String tenantId);
}
