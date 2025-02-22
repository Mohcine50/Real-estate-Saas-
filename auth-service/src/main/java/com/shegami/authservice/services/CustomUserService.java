package com.shegami.authservice.services;




import com.shegami.authservice.UserAccountServiceGrpc;
import com.shegami.authservice.UserRequest;
import com.shegami.authservice.UserResponse;
import com.shegami.authservice.models.AccountDto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.shegami.authservice.Utils.mappers.DtoMappers.grpcRoleListToRoleDtoCollection;


@Service
@Slf4j
public class CustomUserService implements UserDetailsService {


    @GrpcClient("user-service")
    private UserAccountServiceGrpc.UserAccountServiceBlockingStub userAccountServiceStub;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserRequest request = UserRequest.newBuilder().setEmail(email).build();

        // Call the gRPC service
        UserResponse userResponse = userAccountServiceStub.getUser(request);

        if (userResponse == null) return null;


        var user = new AccountDto.Builder()
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .password(userResponse.getPassword())
                .types(grpcRoleListToRoleDtoCollection(userResponse.getTypesList())).build();

        Collection<GrantedAuthority> authorities = user.getTypes().stream()
                .map(type -> new SimpleGrantedAuthority(String.valueOf(type.getName()))).collect(Collectors.toList());


        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }



}