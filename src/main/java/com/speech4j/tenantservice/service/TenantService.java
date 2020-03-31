package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.Tenant;

import java.util.List;

public interface TenantService extends EntityService<Tenant>{
    List<Tenant> findAll();
}
