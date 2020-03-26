package com.speech4j.tenantservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.speech4j.tenantservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResp {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp updatedDate;
    private boolean active;
}