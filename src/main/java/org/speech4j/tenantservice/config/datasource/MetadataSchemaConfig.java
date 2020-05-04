package org.speech4j.tenantservice.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.speech4j.tenantservice.repository.metadata",
        entityManagerFactoryRef = "metadataEntityManagerFactory",
        transactionManagerRef = "metadataTransactionManager"
)
public class MetadataSchemaConfig {
    private final JpaProperties jpaProperties;
    private final DataSource dataSource;

    @Autowired
    public MetadataSchemaConfig(JpaProperties jpaProperties,
                               DataSource dataSource) {
        this.jpaProperties = jpaProperties;
        this.dataSource = dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean metadataEntityManagerFactory() {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put("hibernate.default_schema", "metadata");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("/org/speech4j/tenantservice/entity/metadata");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager metadataTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(metadataEntityManagerFactory().getObject());
        return transactionManager;
    }
}
