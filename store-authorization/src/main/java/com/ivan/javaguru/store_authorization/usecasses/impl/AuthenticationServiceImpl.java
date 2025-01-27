package com.ivan.javaguru.store_authorization.usecasses.impl;

import com.ivan.javaguru.store_authorization.config.BeanName;
import com.ivan.javaguru.store_authorization.persistence.model.User;
import com.ivan.javaguru.store_authorization.usecasses.AuthenticationService;
import com.ivan.javaguru.store_authorization.usecasses.UserService;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationUserDto;
import com.ivan.javaguru.store_authorization.usecasses.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Qualifier(BeanName.B_CRYPT_PASSWORD_ENCODER)
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationUserDto findByEmailAndPassword(AuthenticationRequestDto requestDto) {
        Optional<User> o = userService.findByEmail(requestDto.getEmail());
        if (o.isPresent() && passwordEncoder.matches(requestDto.getPassword(), o.get().getPassword())) {
            AuthenticationUserDto authenticationUserDto = userMapper.userToAuthenticationUserDto(o.get());
            log.info("IN findByEmailAndPassword - authenticationUserDto: {} found by email: {}", authenticationUserDto,
                    authenticationUserDto.getEmail());
            return authenticationUserDto;
        }
        log.info("IN findByEmailAndPassword - Invalid username or password");
        return null;
    }
}
