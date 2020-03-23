package com.speech4j.tenantservice.dto;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto {
    private Long id;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String name;
    private Timestamp createdDate;
    private boolean active;
}
