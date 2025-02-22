package com.shegami.usermanagementservice.services.rest;



import com.shegami.usermanagementservice.entities.Account;
import com.shegami.usermanagementservice.entities.AccountType;
import com.shegami.usermanagementservice.models.RegisterDto;
import com.shegami.usermanagementservice.models.Type;

import java.util.List;

public interface AccountService {
    Account addNewUser(RegisterDto registerDto);

    Account getUserById(String id);


    AccountType addNewType(AccountType role);

    void addRoleToUser(String username, Type roleName);

    void deleteRoleFromUser(String email, Type roleName);

    Account loadUserByEmail(String email);

    List<Account> listUser();

    void deleteRole(String id);

    void deleteUser(String id);
}