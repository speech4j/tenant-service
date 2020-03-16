package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, UserRepository> {
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
