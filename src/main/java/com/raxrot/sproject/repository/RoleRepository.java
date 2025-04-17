package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.AppRole;
import com.raxrot.sproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
