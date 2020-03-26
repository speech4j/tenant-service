package com.speech4j.tenantservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDtoResp {
    private Long id;
    private String apiName;
    private String username;
    private String password;
}
