package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantService extends AbstractService<Tenant, TenantRepository> {
    public TenantService(TenantRepository tenantRepository) {
        super(tenantRepository);
    }
}
