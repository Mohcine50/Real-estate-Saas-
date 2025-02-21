package com.shegami.usermanagementservice.services.rest;

import com.shegami.usermanagementservice.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {


    private final AccountService accountService;

    public CustomUserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = accountService.loadUserByUsername(username);

        Collection<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }


}