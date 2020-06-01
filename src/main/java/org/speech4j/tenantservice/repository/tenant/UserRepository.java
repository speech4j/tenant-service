package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAllByTenantId(String tenantId);
    @Modifying
    @Transactional
    @Query(value = "update User u set u.active = 'false' where u.id = :id")
    void deactivate(String id);
}
