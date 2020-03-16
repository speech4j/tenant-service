package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.service.ConfigService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configs")
public class ConfigController extends AbstractController<Config, ConfigService> {
    protected ConfigController(ConfigService service) {
        super(service);
    }
}
