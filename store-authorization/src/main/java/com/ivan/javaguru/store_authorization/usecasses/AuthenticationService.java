package com.ivan.javaguru.store_authorization.usecasses;

import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationUserDto;

public interface AuthenticationService {
    AuthenticationUserDto findByEmailAndPassword(AuthenticationRequestDto requestDto);
}
