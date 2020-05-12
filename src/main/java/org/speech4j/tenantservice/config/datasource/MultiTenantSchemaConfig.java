package org.speech4j.tenantservice.config.datasource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.speech4j.tenantservice.repository.tenant",
        entityManagerFactoryRef = "multitenantEntityManagerFactory",
        transactionManagerRef = "multitenantTransactionManager"
)
@EnableTransactionManagement
public class MultiTenantSchemaConfig {
    private final JpaProperties jpaProperties;
    private final DataSource dataSource;
    private MultiTenantConnectionProvider multiTenantConnectionProviderImpl;
    private CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl;

    public MultiTenantSchemaConfig(JpaProperties jpaProperties, DataSource dataSource,
                                   MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
                                   CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
        this.jpaProperties = jpaProperties;
        this.dataSource = dataSource;
        this.multiTenantConnectionProviderImpl = multiTenantConnectionProviderImpl;
        this.currentTenantIdentifierResolverImpl = currentTenantIdentifierResolverImpl;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean multitenantEntityManagerFactory() {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("/org/speech4j/tenantservice/entity/tenant");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager multitenantTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(multitenantEntityManagerFactory().getObject());
        return transactionManager;
    }
}
