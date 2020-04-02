package com.speech4j.tenantservice.dto.request;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDtoReq {
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    private String apiName;
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    private String username;
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$",
            message = "{password.validation}")
    private String password;
}
