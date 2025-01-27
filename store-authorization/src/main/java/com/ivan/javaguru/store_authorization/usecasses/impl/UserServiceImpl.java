package com.ivan.javaguru.store_authorization.usecasses.impl;

import com.ivan.javaguru.store_authorization.config.BeanName;
import com.ivan.javaguru.store_authorization.exception.BadRequestException;
import com.ivan.javaguru.store_authorization.persistence.model.Role;
import com.ivan.javaguru.store_authorization.persistence.model.RoleNameEnum;
import com.ivan.javaguru.store_authorization.persistence.model.User;
import com.ivan.javaguru.store_authorization.persistence.repository.RoleRepo;
import com.ivan.javaguru.store_authorization.persistence.repository.UserRepo;
import com.ivan.javaguru.store_authorization.usecasses.UserService;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationRequestDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.UserResponseDto;
import com.ivan.javaguru.store_authorization.usecasses.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Qualifier(BeanName.B_CRYPT_PASSWORD_ENCODER)
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponseDto save(AuthenticationRequestDto dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("User with the same email already exists, email: %s ".formatted(dto.getEmail()));
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(encodedPassword);
        List<Role> roles = roleRepo.findByName(RoleNameEnum.ROLE_USER);

        newUser.setRoles(roles);
        newUser = userRepo.save(newUser);
        return userMapper.toUserResponseDto(newUser);
    }
}
