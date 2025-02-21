package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findByUsername(String username);
}
