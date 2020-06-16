package org.speech4j.tenantservice.service;

import reactor.core.publisher.Mono;

public interface EntityService<E, D> {
    Mono<D> create(E entity, String... ids);

    Mono<D> getById(String... ids);

    Mono<D> update(E entity, String... ids);

    Mono<Void> deleteById(String... ids);
}