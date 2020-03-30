package com.speech4j.tenantservice.dto.request;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDtoReq {
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String name;
}
