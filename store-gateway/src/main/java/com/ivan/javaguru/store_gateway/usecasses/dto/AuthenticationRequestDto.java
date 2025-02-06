package com.ivan.javaguru.store_gateway.usecasses.dto;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
