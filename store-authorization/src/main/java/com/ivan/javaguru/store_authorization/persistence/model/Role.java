package com.ivan.javaguru.store_authorization.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleNameEnum name;
}
