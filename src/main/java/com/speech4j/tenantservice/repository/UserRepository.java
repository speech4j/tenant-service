package com.speech4j.tenantservice.repository;

import com.speech4j.tenantservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,String> {
    List<User> findAllByTenantId(String tenantId);
}
