package org.speech4j.tenantservice.repository.tenant;

import org.speech4j.tenantservice.entity.tenant.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAllByTenantId(String tenantId);
}
