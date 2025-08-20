package com.crud.crud_lombok_dto.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "Enter a secure password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#&]).{6,}$",
            message = "Password must be at least 6 characters and include uppercase, lowercase, digit and special character (@, #, &)"
    )
    private String newPassword;
    private String confirmPassword;
}
