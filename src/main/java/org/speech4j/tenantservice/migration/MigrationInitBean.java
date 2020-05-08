package org.speech4j.tenantservice.migration;

import org.speech4j.tenantservice.config.multitenancy.MultiTenantConnectionProviderImpl;
import org.speech4j.tenantservice.migration.service.InitService;
import org.speech4j.tenantservice.migration.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@ConditionalOnBean(MultiTenantConnectionProviderImpl.class)
@Component
public class MigrationInitBean {

    private InitService initService;
    private SourceService sourceService;

    @Autowired
    public MigrationInitBean(InitService initService,
                             SourceService sourceService) {
        this.initService = initService;
        this.sourceService = sourceService;
    }

    @PostConstruct
    public void init(){
        sourceService.insertDefaultData();
        List<String> tenants = sourceService.getAllTenantIdentifiers();
        //Migration of schemas for tenants
        initService.initSchema(tenants);
    }
}