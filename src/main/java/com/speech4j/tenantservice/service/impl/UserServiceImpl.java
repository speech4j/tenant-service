package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Role;
import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.exception.UserNotFoundException;
import com.speech4j.tenantservice.repository.UserRepository;
import com.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User create(User entity) {
        //Setting a current date
        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        entity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        //Encoding password before saving
        entity.setPassword(encoder.encode(entity.getPassword()));
        //Making status active
        entity.setActive(true);
        //Checking if role is missed
        if (entity.getRole() == null) {
            entity.setRole(Role.ADMIN);
        }

        return repository.save(entity);
    }

    @Override
    public User findById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public User update(User entity, String id) {
        User user = findByIdOrThrowException(id);
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        //Encoding password before updating
        user.setPassword(encoder.encode(entity.getPassword()));
        //Setting a current date
        user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return repository.save(user);
    }

    @Override
    public void deleteById(String id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<User> findAllById(String id) {
        if (repository.findAllByTenantId(id).size() != 0){
            return repository.findAllByTenantId(id);
        }else {
            throw new UserNotFoundException("User not found!");
        }
    }

    private User findByIdOrThrowException(String id) {
        //Checking if user is found
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
