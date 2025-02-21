package com.shegami.usermanagementservice.services.rest;



import com.shegami.usermanagementservice.entities.AppUser;
import com.shegami.usermanagementservice.entities.Role;
import com.shegami.usermanagementservice.models.RegisterDto;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(RegisterDto registerDto);

    AppUser getUserById(String id);

    AppUser getUserByUsername(String username);

    Role addNewRole(Role role);

    void addRoleToUser(String username, String roleName);

    void deleteRoleFromUser(String username, String roleName);

    AppUser loadUserByUsername(String username);

    List<AppUser> listUser();

    void deleteRole(String id);

    void deleteUser(String id);
}