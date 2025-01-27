package com.ivan.javaguru.store_authorization.usecasses.dto;

import com.ivan.javaguru.store_authorization.util.ValidationConstants;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class AuthenticationRequestDto {
    @Pattern(regexp = ValidationConstants.REGEXP_VALIDATE_EMAIL, message = "Invalid e-mail address")
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;
}
