package org.speech4j.tenantservice.repository;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, String> {
}
