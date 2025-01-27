package com.ivan.javaguru.store_authorization.usecasses.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenValidRequestDto(
        @NotBlank(message = "Token is mandatory!")
        String token
) {
}
