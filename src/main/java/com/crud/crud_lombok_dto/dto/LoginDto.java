package com.crud.crud_lombok_dto.dto;

import com.crud.crud_lombok_dto.config.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginDto {

    @NotEmpty(message = "Email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Role is required")
    private RoleType role;
}
