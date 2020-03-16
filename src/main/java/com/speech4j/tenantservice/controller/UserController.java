package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<User, UserService> {
    public UserController(UserService service) {
        super(service);
    }
}
