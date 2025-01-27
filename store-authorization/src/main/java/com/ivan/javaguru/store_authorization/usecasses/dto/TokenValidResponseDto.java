package com.ivan.javaguru.store_authorization.usecasses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidResponseDto {
    private Boolean isValid;
    private String message;
}
