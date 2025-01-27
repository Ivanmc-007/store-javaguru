package com.ivan.javaguru.store_authorization.usecasses;

import com.ivan.javaguru.store_authorization.persistence.model.User;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.UserResponseDto;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    UserResponseDto save(AuthenticationRequestDto dto);
}
