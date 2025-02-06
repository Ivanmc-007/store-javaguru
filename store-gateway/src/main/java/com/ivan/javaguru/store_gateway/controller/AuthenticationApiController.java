package com.ivan.javaguru.store_gateway.controller;

import com.ivan.javaguru.store_gateway.client.KeycloakAuthClient;
import com.ivan.javaguru.store_gateway.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_gateway.usecasses.dto.KeycloakAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationApiController {

    private final KeycloakAuthClient keycloakAuthClient;

    @PostMapping("/access-token")
    public Mono<KeycloakAuthResponseDto> getAccessToken(@RequestBody AuthenticationRequestDto dto) {
        return keycloakAuthClient.authenticate(dto);
    }
}
