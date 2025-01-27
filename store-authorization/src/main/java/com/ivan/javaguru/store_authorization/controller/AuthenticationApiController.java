package com.ivan.javaguru.store_authorization.controller;

import com.ivan.javaguru.store_authorization.exception.BadRequestException;
import com.ivan.javaguru.store_authorization.security.jwt.JwtTokenProvider;
import com.ivan.javaguru.store_authorization.usecasses.UserService;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationUserDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.TokenValidRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.TokenValidResponseDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.UserResponseDto;
import com.ivan.javaguru.store_authorization.usecasses.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationServiceImpl authenticationService;
    private final UserService userService;

    // Authenticate user (return token)
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthenticationRequestDto requestDto) {
        AuthenticationUserDto user = authenticationService.findByEmailAndPassword(requestDto);
        if (user == null) {
            throw new BadRequestException("Wrong email or password");
        }
        Map<String, String> response = new HashMap<>();
        response.put("username", user.getEmail());
        response.put("token", jwtTokenProvider.createToken(user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/is-token-valid")
    public ResponseEntity<TokenValidResponseDto> isTokenValid(@Valid @RequestBody TokenValidRequestDto requestDto) {
        TokenValidResponseDto responseDto = jwtTokenProvider.isTokenValid(requestDto.token());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody AuthenticationRequestDto requestDto) {
        UserResponseDto responseDto = userService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
