package com.shegami.authservice.services;

import com.shegami.authservice.*;
import com.shegami.authservice.models.AccountDto;
import com.shegami.authservice.models.RegisterDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import static com.shegami.authservice.Utils.mappers.DtoMappers.grpcRoleListToRoleDtoCollection;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private UserAccountServiceGrpc.UserAccountServiceBlockingStub userAccountServiceStub;


    @Override
    public AccountDto findUserByEmail(String email) {
        UserRequest request = UserRequest.newBuilder().setEmail(email).build();


        // Call the gRPC service
        UserResponse userResponse = userAccountServiceStub.getUser(request);

        log.info("Registering user: {}", userResponse);

        String id = userResponse.getId();
        if (id.isEmpty()) return null;


        return new AccountDto.Builder()
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .password(userResponse.getPassword())
                .types(grpcRoleListToRoleDtoCollection(userResponse.getTypesList())).build();
    }

    @Override
    public Boolean registerNewUser(RegisterDto registerDto) {

        AddNewUserRequest userRequest = AddNewUserRequest.newBuilder()
                .setEmail(registerDto.getEmail())
                .setPassword(registerDto.getPassword())
                .build();

        AddNewUserResponse newUserResponse = userAccountServiceStub.addNewUser(userRequest);

        return newUserResponse != null && newUserResponse.getCreated();

    }
}
