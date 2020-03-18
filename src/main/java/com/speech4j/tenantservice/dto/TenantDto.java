package com.speech4j.tenantservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.ZonedDateTime;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_EMPTY;
import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
@Builder
public class TenantDto {
    @Null(groups = {NewData.class}, message = REQUIRED_EMPTY)
    private Long id;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private ZonedDateTime createdDate;
    private boolean active;
}
