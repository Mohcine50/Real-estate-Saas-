package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.AccountType;
import com.shegami.usermanagementservice.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AccountType, String> {
    AccountType findByName(String name);
}
