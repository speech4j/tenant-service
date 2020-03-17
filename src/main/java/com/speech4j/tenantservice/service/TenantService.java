package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.Tenant;


public interface TenantService {
    Tenant findById(Long id);

}
