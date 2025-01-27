package com.ivan.javaguru.store_authorization.persistence.repository;

import com.ivan.javaguru.store_authorization.persistence.model.Role;
import com.ivan.javaguru.store_authorization.persistence.model.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    List<Role> findByName(RoleNameEnum name);
}
