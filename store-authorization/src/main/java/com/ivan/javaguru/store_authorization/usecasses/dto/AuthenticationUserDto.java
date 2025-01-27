package com.ivan.javaguru.store_authorization.usecasses.dto;

import com.ivan.javaguru.store_authorization.persistence.model.RoleNameEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticationUserDto {
    private String email;
    private List<RoleNameEnum> roles;
}
