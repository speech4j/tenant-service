package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.Role;
import org.speech4j.tenantservice.entity.tenant.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    @Query("INSERT INTO users (id, active, createddate, modifieddate, email, firstname, lastname, password, role, tenantid) " +
            "VALUES (:id, :active, :createdDate, :modifiedDate, :email, :firstName, :lastName, :password, :role, :tenantId)"
          //  "WHERE (SELECT * FROM users WHERE email = :email) = 0"
    )
    Mono<User> create(String id, Boolean active, LocalDate createdDate, LocalDate modifiedDate, String email,
                      String firstName, String lastName, String password, Role role, String tenantId);

    @Query(value = "UPDATE users SET active = 'false' WHERE id = :id")
    Mono<Void> deactivate(String id);

    Flux<User> getAllByTenantId(String tenantId);

}
