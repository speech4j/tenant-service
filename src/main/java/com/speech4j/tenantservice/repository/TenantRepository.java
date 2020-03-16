package com.speech4j.tenantservice.repository;

import com.speech4j.tenantservice.entity.Tenant;
import org.springframework.stereotype.Repository;


@Repository
public interface TenantRepository extends CommonRepository<Tenant> {
}
