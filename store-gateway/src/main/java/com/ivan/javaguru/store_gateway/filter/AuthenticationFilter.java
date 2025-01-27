package com.ivan.javaguru.store_gateway.filter;

import com.ivan.javaguru.store_gateway.client.AuthorizationServiceClient;
import com.ivan.javaguru.store_gateway.usecasses.dto.TokenValidResponseDto;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private AuthorizationServiceClient authorizationServiceClient;

    public static class Config {

    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtil.isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                return handleError(exchange, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            Mono<TokenValidResponseDto> mono = authorizationServiceClient.isTokenValid(token);
            return mono
                    .flatMap(response -> {
                        if (response.getIsValid())
                            return chain.filter(exchange);
                        log.error("invalid access...!");
                        log.error(response.getMessage());
                        return Mono.error(new RuntimeException(response.getMessage()));
                    })
                    .onErrorResume(e -> handleError(exchange, "Not Authorized. Access denied: " + e.getMessage()));
        });
    }

    private Mono<Void> handleError(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain");
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = bufferFactory.wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
