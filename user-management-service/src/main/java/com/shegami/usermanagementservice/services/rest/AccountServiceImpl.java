package com.shegami.usermanagementservice.services.rest;


import com.shegami.usermanagementservice.entities.AppUser;
import com.shegami.usermanagementservice.entities.Role;
import com.shegami.usermanagementservice.exceptions.ApiRequestException;
import com.shegami.usermanagementservice.exceptions.NotFoundException;
import com.shegami.usermanagementservice.models.RegisterDto;
import com.shegami.usermanagementservice.repositories.AppUserRepository;
import com.shegami.usermanagementservice.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(RegisterDto registerDto) {


        AppUser user = appUserRepository.findByUsername(registerDto.getUsername());
        if (user != null) {
            throw new ApiRequestException("Username Already Exist please try other one");
        }

        Role role = roleRepository.findByName("USER");

        AppUser newUser = AppUser.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .roles(List.of(role))
                .build();


        appUserRepository.save(newUser);


        return newUser;
    }

    @Override
    public AppUser getUserById(String id) {
        AppUser user = appUserRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException("No User Found With ID: " + id);
        }

        return user;
    }

    @Override
    public AppUser getUserByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username);

        if (user == null) {
            throw new NotFoundException("No User Found With Username: " + username);
        }

        return user;
    }

    @Override
    public Role addNewRole(Role role) {

        Role role1 = roleRepository.findByName(role.getName());
        if (role1 != null) {
            throw new ApiRequestException("Role Already exist");
        }

        Role newRole = Role.builder()
                .name(role.getName())
                .build();


        roleRepository.save(newRole);

        return newRole;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("User Not Found");
        Role role = roleRepository.findByName(roleName);
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new ArrayList<>(List.of(role));
        } else {
            roles.add(role);
        }

    }

    @Override
    public void deleteRoleFromUser(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("User Not Found");
        Role role = roleRepository.findByName(roleName);
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<Role> roles = user.getRoles();
        roles.remove(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUser() {
        return appUserRepository.findAll();
    }

    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String id) {
        appUserRepository.deleteById(id);
    }
}
