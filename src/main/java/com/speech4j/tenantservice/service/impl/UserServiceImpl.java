package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Role;
import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.exception.CrudException;
import com.speech4j.tenantservice.exception.UserNotFoundException;
import com.speech4j.tenantservice.repository.UserRepository;
import com.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        //Encoding password before saving
        entity.setPassword(encoder.encode(entity.getPassword()));
        //Checking if role is missed
        if (entity.getRole() == null) {
            entity.setRole(Role.ADMIN);
        }

        try {
            return repository.save(entity);
        }catch (RuntimeException e){
            throw new CrudException("Error during the creating of user because of the duplicate value email: {" + entity.getEmail() + "}");
        }

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

        return repository.save(user);
    }

    @Override
    public void deleteById(String id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<User> findAllById(String id) {
        List<User> list = repository.findAllByTenantId(id);
        if (!list.isEmpty()){
            return list;
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
