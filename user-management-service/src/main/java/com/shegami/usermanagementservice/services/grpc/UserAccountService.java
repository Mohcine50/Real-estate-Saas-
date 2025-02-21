package com.shegami.usermanagementservice.services.grpc;



import com.shegami.usermanagementservice.AddNewUserRequest;
import com.shegami.usermanagementservice.AddNewUserResponse;
import com.shegami.usermanagementservice.Role;
import com.shegami.usermanagementservice.UserAccountServiceGrpc;
import com.shegami.usermanagementservice.UserRequest;
import com.shegami.usermanagementservice.UserResponse;
import com.shegami.usermanagementservice.entities.AppUser;
import com.shegami.usermanagementservice.models.RegisterDto;
import com.shegami.usermanagementservice.services.rest.AccountService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
public class UserAccountService extends UserAccountServiceGrpc.UserAccountServiceImplBase {

    @Autowired
    private AccountService accountService;

    @Override
    public void addNewUser(AddNewUserRequest request, StreamObserver<AddNewUserResponse> responseObserver) {


        RegisterDto registerDto = RegisterDto.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();

        var user = accountService.addNewUser(registerDto);


        responseObserver.onNext(
                AddNewUserResponse.newBuilder().setCreated(user != null).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {

        AppUser appUser = accountService.getUserByUsername(request.getUsername());

        UserResponse userResponse = null;


        if(appUser != null) {

            Iterable<Role> roles = appUser.getRoles().stream()
                    .map(role -> Role.newBuilder()
                            .setId(role.getId())
                            .setName(role.getName())
                            .build())
                    .collect(Collectors.toList());

            userResponse = UserResponse.newBuilder()
                    .setUsername(appUser.getUsername())
                    .setEmail(appUser.getEmail())
                    .setPassword(appUser.getPassword())
                    .setId(appUser.getId())
                    .addAllRoles(roles)
                    .build();
        }

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }



}
