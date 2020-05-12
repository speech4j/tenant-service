package org.speech4j.tenantservice.service;

import org.speech4j.tenantservice.entity.tenant.User;

import java.util.List;

public interface UserService extends EntityService<User> {
    List<User> findAllById(String id);
}
