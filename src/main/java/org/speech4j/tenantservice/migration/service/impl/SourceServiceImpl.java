package org.speech4j.tenantservice.migration.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.entity.tenant.Config;
import org.speech4j.tenantservice.entity.tenant.User;
import org.speech4j.tenantservice.migration.service.SourceService;
import org.speech4j.tenantservice.service.ConfigService;
import org.speech4j.tenantservice.service.UserService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SourceServiceImpl implements SourceService {
    private UserService userService;
    private ConfigService configService;

    public SourceServiceImpl(UserService userService, ConfigService configService) {
        this.userService = userService;
        this.configService = configService;
    }

    @Override
    public void insertDefaultDataForTenant() {
        User user = new User();
        user.setActive(true);
        user.setEmail("speech4j@gmail.com");
        user.setFirstName("speech4j_name");
        user.setLastName("speech4j_surname");
        user.setPassword("password");
        user.setTenantId("speech4j");
        userService.create(user);
        log.debug("SOURCE-SERVICE: Default user was successfully set!");

        Config config = new Config();
        config.setApiName("AWS");
        config.setUsername("speech4j@gmail.com");
        config.setPassword("password");
        configService.create(config);
        log.debug("SOURCE-SERVICE: Default config was successfully set!");
    }
}
