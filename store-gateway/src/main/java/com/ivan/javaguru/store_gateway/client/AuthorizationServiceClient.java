package com.ivan.javaguru.store_gateway.client;

import com.ivan.javaguru.store_gateway.config.BeanName;
import com.ivan.javaguru.store_gateway.usecasses.dto.TokenValidRequestDto;
import com.ivan.javaguru.store_gateway.usecasses.dto.TokenValidResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationServiceClient {

    @Qualifier(BeanName.AUTHORIZATION_WEB_CLIENT_BUILDER)
    private final WebClient.Builder webClientBuilder;

    public Mono<TokenValidResponseDto> isTokenValid(String token) {
        return webClientBuilder.build()
                .post()
                .uri("http://STORE-AUTHORIZATION/store-authorization/api/v1/auth/is-token-valid")
                .bodyValue(new TokenValidRequestDto(token))
                .retrieve()
                .bodyToMono(TokenValidResponseDto.class);
    }
}
