package org.speech4j.tenantservice.config.multitenancy;

public final class MultiTenantConstants{
    public static final String DEFAULT_TENANT_ID = "speech4j";
    public static final String DEFAULT_METADATA = "metadata";

    public static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS %s";
    public static final String TENANT_KEY = "tenant-key";
    private MultiTenantConstants(){}
}