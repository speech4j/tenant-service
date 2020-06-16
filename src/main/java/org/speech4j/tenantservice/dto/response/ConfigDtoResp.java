package org.speech4j.tenantservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.speech4j.tenantservice.entity.tenant.ApiName;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ConfigDtoResp {
    private String id;
    private ApiName apiName;
    private Map<String, Object> credentials;
    private String tenantId;
}
