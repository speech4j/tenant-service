package com.speech4j.tenantservice.dto.request;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDtoReq {
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String apiName;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String username;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    @Size(groups = {NewData.class, ExistData.class}, min = 6, message = "Password must be at least 6 symbols long")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[A-Za-z]+.*",
            message = "Pattern must contain at least 1 alphabetical character")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[0-9]+.*",
            message = "Pattern must contain at least 1 numeric character")
    private String password;
}
