package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.entity.tenant.Role;
import org.speech4j.tenantservice.entity.tenant.User;
import org.speech4j.tenantservice.exception.UserNotFoundException;
import org.speech4j.tenantservice.repository.tenant.UserRepository;
import org.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           PasswordEncoder encoder) {
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
         User user = repository.save(entity);
         log.debug("USER-SERVICE: User with [ id: {}] was successfully created!", entity.getId());
         return user;
    }

    @Override
    public User findById(String id) {
        User user = findByIdOrThrowException(id);
        log.debug("USER-SERVICE: User with [ id: {}] was successfully found!", id);
        return user;
    }

    @Override
    public User update(User entity, String id) {
        User user = findByIdOrThrowException(id);
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        //Encoding password before updating
        user.setPassword(encoder.encode(entity.getPassword()));

        User updatedUser = repository.save(user);
        log.debug("USER-SERVICE: User with [ id: {}] was successfully updated!", id);
        return updatedUser;
    }

    @Override
    public void deleteById(String id) {
        User user = findByIdOrThrowException(id);
        user.setActive(false);
        repository.save(user);
        log.debug("USER-SERVICE: User with [ id: {}] was successfully deleted!", id);
    }

    @Override
    public List<User> findAllById(String id) {
        List<User> list = repository.findAllByTenantId(id).stream().filter(User::isActive).collect(Collectors.toList());
        if (!list.isEmpty()){
            log.debug("USER-SERVICE: Users with [ tenantId: {}] were successfully found!", id);
            return list;
        }else {
            throw new UserNotFoundException("User not found!");
        }
    }

    private User findByIdOrThrowException(String id) {
        //Checking if user is found
        return repository.findById(id).filter(User::isActive)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
