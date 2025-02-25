package com.ivan.javaguru.store_gateway.usecasses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakAuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("session_state")
    private String sessionState;

    @JsonProperty("scope")
    private String scope;
}
