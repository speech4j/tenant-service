package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.metadata.Tenant;

import java.util.List;

public interface TenantService extends EntityService<Tenant>{
    List<Tenant> findAll();
}
