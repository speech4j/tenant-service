package org.speech4j.tenantservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDtoReq {
    @NotBlank(message = "{field.not.empty}")
    private String apiName;
    private Map<String, Object> credentials;
}
