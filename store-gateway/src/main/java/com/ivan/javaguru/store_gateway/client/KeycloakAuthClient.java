package com.ivan.javaguru.store_gateway.client;

import com.ivan.javaguru.store_gateway.config.BeanName;
import com.ivan.javaguru.store_gateway.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_gateway.usecasses.dto.KeycloakAuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakAuthClient {

    @Qualifier(BeanName.REST_TEMPLATE)
    private final RestTemplate restTemplate;

    @Value("${spring.main.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.main.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.main.security.oauth2.client.registration.keycloak.token-uri}")
    private String tokenUrl;

    public Mono<KeycloakAuthResponseDto> authenticate(AuthenticationRequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("grant_type", "password");
        parameters.add("username", dto.username());
        parameters.add("password", dto.password());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        KeycloakAuthResponseDto responseDto = restTemplate
                .exchange(tokenUrl,
                        HttpMethod.POST,
                        entity,
                        KeycloakAuthResponseDto.class)
                .getBody();
        return Mono.just(responseDto);
    }

}
