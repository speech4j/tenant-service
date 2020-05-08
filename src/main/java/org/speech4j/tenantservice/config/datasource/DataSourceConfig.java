package org.speech4j.tenantservice.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.SQL_CREATE_SCHEMA;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.driver}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${jpa.metadata_default_schema}")
    private String metadataSchema;

    private void init(DataSource dataSource){
        try (final Connection connection = dataSource.getConnection()){
            try(Statement st = connection.createStatement()) {
               st.executeUpdate(String.format(SQL_CREATE_SCHEMA, metadataSchema));
            }
        }catch (SQLException e){
            log.debug(e.getMessage());
        }

    }

    @Bean
    public DataSource getDatasource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(driverClassName);
        dataSourceConfig.setJdbcUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setMaximumPoolSize(5);

        DataSource dataSource = new HikariDataSource(dataSourceConfig);
        //Initializing of speech4j schema
        init(dataSource);

        return dataSource;
    }
}
