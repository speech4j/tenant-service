package org.speech4j.tenantservice.migration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@ConditionalOnBean(MultiTenantConnectionProviderImpl.class)
@Component
@Slf4j
public class MigrationInitBean {

    //    private InitService initService;
//    private MetadataService metadataService;
//    private SourceService sourceService;
//
//    @Autowired
//    public MigrationInitBean(InitService initService,
//                             MetadataService metadataService,
//                             SourceService sourceService
//    ) {
//        this.initService = initService;
//        this.metadataService = metadataService;
//        this.sourceService = sourceService;
//    }

    @PostConstruct
    public void init(){
//        try {
//            metadataService.insertDefaultTenant();
//            sourceService.insertDefaultDataForTenant();
//        }catch (DuplicateEntityException e) {
//            log.debug("MIGRATION-BEAN: Default tenant was successfully set!");
//        }
//        List<String> tenants = metadataService.getAllTenantIdentifiers();
//        //Migration of schemas for tenants
//        initService.initSchema(tenants);



    }

//    private void setTenantHolder() {
//        PostgresqlConnectionFactory cf = new PostgresqlConnectionFactory(builder.schema(DEFAULT_METADATA).build());
//        DatabaseClient cli = DatabaseClient.create(cf);
//        Flux<Tenant> tenants = cli.execute("select * from tenants").as(Tenant.class).fetch().all();
//    }
}
