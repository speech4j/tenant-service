package org.speech4j.tenantservice.repository;

import org.speech4j.tenantservice.entity.general.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,String> {
    List<User> findAllByTenantId(String tenantId);

    //@Modifying
   // @Query( value = "UPDATE User u SET u.active = false WHERE u.tenant.id = :tenantId")
    void deleteAllByTenantId(String tenantId);
}
