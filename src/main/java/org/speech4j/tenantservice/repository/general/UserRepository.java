package org.speech4j.tenantservice.repository.general;

import org.speech4j.tenantservice.entity.general.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAllByTenantId(String tenantId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.active = false WHERE u.tenantId = :tenantId")
    void deleteAllByTenantId(String tenantId);
}
