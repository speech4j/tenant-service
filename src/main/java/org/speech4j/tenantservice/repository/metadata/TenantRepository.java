package org.speech4j.tenantservice.repository.metadata;

import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.springframework.data.repository.CrudRepository;

public interface TenantRepository extends CrudRepository<Tenant, String> {
}
