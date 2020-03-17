package com.speech4j.tenantservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import com.speech4j.tenantservice.entity.Tenant;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_EMPTY;
import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
public class ConfigDto {
    private Long id;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String apiName;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String username;
    @NotNull(groups = {NewData.class}, message = REQUIRED_NOT_EMPTY)
    @Size(groups = {NewData.class}, min = 6, message = "Password must be at least 6 symbols long")
    @Pattern(groups = {NewData.class}, regexp = ".*[A-Za-z]+.*",
            message = "Pattern must contain at least 1 alphabetical character")
    @Pattern(groups = {NewData.class}, regexp = ".*[0-9]+.*",
            message = "Pattern must contain at least 1 numeric character")
    @Null(groups = {ExistData.class}, message = REQUIRED_EMPTY)
    private String password;
}
