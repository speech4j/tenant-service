package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.service.TenantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenants")
public class TenantController extends AbstractController<Tenant, TenantService> {
    public TenantController(TenantService service) {
        super(service);
    }
}
