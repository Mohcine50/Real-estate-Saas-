package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
}
