package com.crud.crud_lombok_dto.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    @NotEmpty(message = "Name cannot be null/blank")
    @Size(min = 2, max = 20, message = "Name must be in range 2 - 20 characters")
    private String name;
    
    @Email
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Enter a secure password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#&]).{6,}$",
            message = "Password must be at least 6 characters and include uppercase, lowercase, digit and special character (@, #, &)"
    )
    private String password;
}
