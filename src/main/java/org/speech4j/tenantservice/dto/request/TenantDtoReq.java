package org.speech4j.tenantservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDtoReq {
    @NotBlank(message = "{field.not.empty}")
    private String name;
    @NotBlank(message = "{field.not.empty}")
    private String description;
}
