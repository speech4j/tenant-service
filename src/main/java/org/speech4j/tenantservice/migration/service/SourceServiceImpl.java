package org.speech4j.tenantservice.migration.service;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {
    private TenantService tenantService;

    @Autowired
    public SourceServiceImpl(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    public void insertDefaultData() {
        Tenant tenant = new Tenant();
        tenant.setId("speech4j");
        tenant.setActive(true);
        tenant.setDescription("Speech4j is a default company.");

        tenantService.create(tenant);
    }

    @Override
    public List<String> getAllTenantIdentifiers(){
        List<String> tenants = new ArrayList<>();

        tenantService.findAll().forEach(tenant -> {
            tenants.add(tenant.getId().replace("-",""));
        });

        return tenants;
    }
}