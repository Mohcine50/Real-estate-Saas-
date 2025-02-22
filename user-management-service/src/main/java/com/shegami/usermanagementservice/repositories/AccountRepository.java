package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);
}
