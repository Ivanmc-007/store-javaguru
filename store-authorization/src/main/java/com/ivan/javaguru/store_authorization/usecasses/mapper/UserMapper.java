package com.ivan.javaguru.store_authorization.usecasses.mapper;

import com.ivan.javaguru.store_authorization.persistence.model.Role;
import com.ivan.javaguru.store_authorization.persistence.model.RoleNameEnum;
import com.ivan.javaguru.store_authorization.persistence.model.User;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationUserDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "roles", expression = "java(rolesToRoleNames(user.getRoles()))")
    public abstract AuthenticationUserDto userToAuthenticationUserDto(User user);

    @Mapping(target = "roles", expression = "java(rolesToRoleNames(user.getRoles()))")
    public abstract UserResponseDto toUserResponseDto(User user);

    protected List<RoleNameEnum> rolesToRoleNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }
}
