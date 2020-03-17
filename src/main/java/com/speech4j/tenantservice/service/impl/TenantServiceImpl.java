package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.repository.TenantRepository;
import com.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {
    private TenantRepository tenantRepository;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant findById(Long id) {
        return tenantRepository.findById(id).orElseThrow(()->new RuntimeException("Tenant not found"));
    }
}
