package org.speech4j.tenantservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.speech4j.tenantservice.entity.tenant.ApiName;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDtoReq {
    @NotNull(message = "{field.not.empty}")
    private ApiName apiName;
    private Map<String, Object> credentials;
}
