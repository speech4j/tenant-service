package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.general.User;

import java.util.List;

public interface UserService extends EntityService<User> {
    List<User> findAllById(String id);
}
