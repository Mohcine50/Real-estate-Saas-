package com.shegami.gatwayservice.services;

import com.shegami.gatewayservice.UserAccountServiceGrpc;
import com.shegami.gatewayservice.UserRequest;
import com.shegami.gatwayservice.models.AppUserDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.shegami.gatwayservice.utils.mappers.DtoMappers.grpcRoleListToRoleDtoCollection;


@Service
public class CustomUserService implements UserDetailsService {


    @GrpcClient("user-service")
    private UserAccountServiceGrpc.UserAccountServiceBlockingStub userAccountServiceStub;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserRequest request = UserRequest.newBuilder().setUsername(username).build();

        // Call the gRPC service
        com.shegami.gatewayservice.UserResponse userResponse = userAccountServiceStub.getUser(request);

        if (userResponse == null) return null;


        var user = new AppUserDto.Builder()
                .username(userResponse.getUsername())
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .password(userResponse.getPassword())
                .roles(grpcRoleListToRoleDtoCollection(userResponse.getRolesList())).build();

        Collection<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }


}