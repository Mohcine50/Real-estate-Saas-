package com.shegami.usermanagementservice.services.rest;


import com.shegami.usermanagementservice.entities.Account;
import com.shegami.usermanagementservice.entities.AccountType;
import com.shegami.usermanagementservice.exceptions.ApiRequestException;
import com.shegami.usermanagementservice.exceptions.NotFoundException;
import com.shegami.usermanagementservice.models.RegisterDto;
import com.shegami.usermanagementservice.models.Type;
import com.shegami.usermanagementservice.repositories.AccountRepository;
import com.shegami.usermanagementservice.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account addNewUser(RegisterDto registerDto) {

        Account user = accountRepository.findByEmail(registerDto.getEmail());
        if (user != null) {
            throw new ApiRequestException("Username Already Exist please try other one");
        }

        AccountType type = roleRepository.findByName(Type.INDIVIDUAL.name());

        Account newUser = Account.builder()
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .types(List.of(type))
                .build();


        accountRepository.save(newUser);


        return newUser;
    }

    @Override
    public Account getUserById(String id) {
        Account user = accountRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException("No User Found With ID: " + id);
        }

        return user;
    }



    @Override
    public AccountType addNewType(AccountType role) {

        AccountType type = roleRepository.findByName(role.getName());
        if (type != null) {
            throw new ApiRequestException("Role Already exist");
        }

        AccountType newRole = AccountType.builder()
                .name(role.getName())
                .build();


        roleRepository.save(newRole);

        return newRole;
    }

    @Override
    public void addRoleToUser(String email, Type roleName) {

        Account user = accountRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User Not Found");
        AccountType role = roleRepository.findByName(roleName.name());
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<AccountType> types = user.getTypes();
        if (types == null) {
            types = new ArrayList<>(List.of(role));
        } else {
            types.add(role);
        }

    }

    @Override
    public void deleteRoleFromUser(String email, Type roleName) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User Not Found");
        AccountType role = roleRepository.findByName(roleName.name());
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<AccountType> roles = user.getTypes();
        roles.remove(role);
    }

    @Override
    public Account loadUserByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> listUser() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String id) {
        accountRepository.deleteById(id);
    }
}
