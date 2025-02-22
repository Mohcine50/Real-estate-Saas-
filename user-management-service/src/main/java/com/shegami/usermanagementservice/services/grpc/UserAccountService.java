package com.shegami.usermanagementservice.services.grpc;

import com.shegami.usermanagementservice.AddNewUserRequest;
import com.shegami.usermanagementservice.AddNewUserResponse;
import com.shegami.usermanagementservice.UserAccountServiceGrpc;
import com.shegami.usermanagementservice.UserRequest;
import com.shegami.usermanagementservice.UserResponse;
import com.shegami.usermanagementservice.entities.Account;
import com.shegami.usermanagementservice.models.RegisterDto;
import com.shegami.usermanagementservice.Type;
import com.shegami.usermanagementservice.services.rest.AccountService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@GrpcService
@Slf4j
public class UserAccountService extends UserAccountServiceGrpc.UserAccountServiceImplBase {

    @Autowired
    private AccountService accountService;



    @Override
    public void addNewUser(AddNewUserRequest request, StreamObserver<AddNewUserResponse> responseObserver) {


log.info("addNewUser: {}", request);

        RegisterDto registerDto = RegisterDto.builder()
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


        log.info("getUser: {}", request);
        Account appUser = accountService.loadUserByEmail(request.getEmail());
        log.info("appUser: {}", appUser);
        UserResponse userResponse = null;

        if(appUser != null) {

            Iterable<Type> types = appUser.getTypes().stream()
                    .map(role -> Type.newBuilder()
                            .setId(role.getId())
                            .setName(String.valueOf(role.getName()))
                            .build())
                    .collect(Collectors.toList());

            userResponse = UserResponse.newBuilder()
                    .setEmail(appUser.getEmail())
                    .setPassword(appUser.getPassword())
                    .setId(appUser.getId())
                    .addAllTypes(types)
                    .build();
        }
        log.info("userResponse: {}", userResponse);
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }



}
