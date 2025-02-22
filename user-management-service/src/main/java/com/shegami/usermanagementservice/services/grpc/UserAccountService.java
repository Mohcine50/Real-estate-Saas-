package com.shegami.usermanagementservice.services.grpc;

import com.shegami.usermanagementservice.AddNewUserRequest;
import com.shegami.usermanagementservice.AddNewUserResponse;
import com.shegami.usermanagementservice.UserAccountServiceGrpc;
import com.shegami.usermanagementservice.UserRequest;
import com.shegami.usermanagementservice.UserResponse;
import com.shegami.usermanagementservice.entities.Account;
import com.shegami.usermanagementservice.models.AddressDto;
import com.shegami.usermanagementservice.models.ProfileDto;
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

        ProfileDto profile = ProfileDto.builder()
                .address(
                        (!request.getAddress().getZip().isEmpty() || request.getAddress().getZip().isBlank()) ? AddressDto.builder()
                                .city(request.getAddress().getCity())
                                .country(request.getAddress().getCountry())
                                .state(request.getAddress().getState())
                                .zip(request.getAddress().getZip())
                                .street(request.getAddress().getStreet())
                                .build() : null
                )
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        RegisterDto registerDto = RegisterDto.builder()
                .password(request.getPassword())
                .email(request.getEmail())
                .profile(profile)
                .build();

        var user = accountService.registerNewUser(registerDto);


        responseObserver.onNext(
                AddNewUserResponse.newBuilder().setCreated(user != null).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {


        Account appUser = accountService.loadUserByEmail(request.getEmail());


        UserResponse userResponse = null;

        if (appUser != null) {

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
