package org.speech4j.tenantservice.util;

import org.speech4j.tenantservice.exception.ValidationEntityException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class MessageValidationUtil {

    public static <D>Mono validate(D dto){
        Integer counter = 0;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<D>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            StringBuilder messages = new StringBuilder();
            for (ConstraintViolation<D> error: violations) {
                messages.append(++counter + ".").append("Invalid field:[")
                        .append(error.getPropertyPath() + "]")
                        .append(" Message: <= ")
                        .append(error.getMessage() + "=>  ");

            }
            return Mono.error(new ValidationEntityException(messages.toString()));
        }
        return Mono.just(dto);
    }
}
