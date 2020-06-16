package org.speech4j.tenantservice.service;

import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.entity.tenant.User;
import reactor.core.publisher.Flux;

public interface UserService extends EntityService<User, UserDtoResp> {
    Flux<UserDtoResp> getAllById(String tenantId);
}
