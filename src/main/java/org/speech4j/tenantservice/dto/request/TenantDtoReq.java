package org.speech4j.tenantservice.dto.request;

import org.speech4j.tenantservice.dto.validation.ExistData;
import org.speech4j.tenantservice.dto.validation.NewData;
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
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    private String name;
}
