package org.speech4j.tenantservice.config.multitenancy;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.r2dbc.connectionfactory.lookup.ConnectionFactoryLookup;
import org.springframework.data.r2dbc.connectionfactory.lookup.MapConnectionFactoryLookup;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_METADATA;

public abstract class AbstractTenantConnectionFactory<T, R> implements ConnectionFactory, InitializingBean {

    private @Nullable Function<T, R> targetConnectionFactoriesFunction;

    private @Nullable Object defaultTargetConnectionFactory;

    private ConnectionFactoryLookup connectionFactoryLookup = new MapConnectionFactoryLookup();

    private @Nullable ConnectionFactory resolvedDefaultConnectionFactory;

    public void setDefaultTargetConnectionFactory(Object defaultTargetConnectionFactory) {
        Assert.notNull(defaultTargetConnectionFactory, "DefaultTargetConnectionFactory must not be null!");
        this.defaultTargetConnectionFactory = defaultTargetConnectionFactory;
    }

    public void setTargetConnectionFactoriesFunction(@Nullable Function<T, R> targetConnectionFactoriesFunction) {
        Assert.notNull(targetConnectionFactoriesFunction, "targetConnectionFactoriesBiFunction must not be null!");
        this.targetConnectionFactoriesFunction = targetConnectionFactoriesFunction;
    }

    @Nullable
    public Object getDefaultTargetConnectionFactory() {
        return defaultTargetConnectionFactory;
    }

    public void setConnectionFactoryLookup(ConnectionFactoryLookup connectionFactoryLookup) {

        Assert.notNull(connectionFactoryLookup, "ConnectionFactoryLookup must not be null!");

        this.connectionFactoryLookup = connectionFactoryLookup;
    }

    @Override
    public void afterPropertiesSet() {
            this.resolvedDefaultConnectionFactory = resolveSpecifiedConnectionFactory(this.defaultTargetConnectionFactory);
    }

    protected ConnectionFactory resolveSpecifiedConnectionFactory(Object connectionFactory)
            throws IllegalArgumentException {

        if (connectionFactory instanceof ConnectionFactory) {
            return (ConnectionFactory) connectionFactory;
        } else if (connectionFactory instanceof String) {
            return this.connectionFactoryLookup.getConnectionFactory((String) connectionFactory);
        } else {

            throw new IllegalArgumentException(
                    "Illegal connection factory value - only 'io.r2dbc.spi.ConnectionFactory' and 'String' supported: "
                            + connectionFactory);
        }
    }

    @Override
    public Mono<Connection> create() {
        return determineTargetConnectionFactory() //
                .map(ConnectionFactory::create) //
                .flatMap(Mono::from);
    }

    /*
     * (non-Javadoc)
     * @see io.r2dbc.spi.ConnectionFactory#getMetadata()
     */
    @Override
    public ConnectionFactoryMetadata getMetadata() {
            return this.resolvedDefaultConnectionFactory.getMetadata();
    }

    protected Mono<ConnectionFactory> determineTargetConnectionFactory() {

        Assert.state(this.resolvedDefaultConnectionFactory != null, "ConnectionFactory default router not initialized");

        Mono<Object> lookupKey = determineCurrentLookupKey().defaultIfEmpty(DEFAULT_METADATA);

        return lookupKey.handle((key, sink) -> {
            ConnectionFactory connectionFactory = null;
            if (key == DEFAULT_METADATA) {
                connectionFactory = this.resolvedDefaultConnectionFactory;
            }else {
                connectionFactory = (ConnectionFactory) targetConnectionFactoriesFunction.apply((T) key);
            }
            sink.next(connectionFactory);
        });
    }

    /**
     * Determine the current lookup key. This will typically be implemented to check a subscriber context. Allows for
     * arbitrary keys. The returned key needs to match the stored lookup key type, as resolved by the
     *
     * @return {@link Mono} emitting the lookup key. May complete without emitting a value if no lookup key available.
     */
    protected abstract Mono<Object> determineCurrentLookupKey();
}
