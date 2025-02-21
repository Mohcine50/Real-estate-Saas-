package com.shegami.authservice.services;

import com.shegami.authservice.*;
import com.shegami.authservice.models.AppUserDto;
import com.shegami.authservice.models.RegisterDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import static com.shegami.authservice.Utils.mappers.DtoMappers.grpcRoleListToRoleDtoCollection;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient("auth-service")
    private UserAccountServiceGrpc.UserAccountServiceBlockingStub userAccountServiceStub;



    @Override
    public AppUserDto findUserByUsername(String username) {
        UserRequest request = UserRequest.newBuilder().setUsername(username).build();

        // Call the gRPC service
        UserResponse userResponse = userAccountServiceStub.getUser(request);

        if (userResponse == null) return null;


        return new AppUserDto.Builder()
                .username(userResponse.getUsername())
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .password(userResponse.getPassword())
                .roles(grpcRoleListToRoleDtoCollection(userResponse.getRolesList())).build();
    }

    @Override
    public Boolean registerNewUser(RegisterDto registerDto) {

        AddNewUserRequest userRequest = AddNewUserRequest.newBuilder()
                .setUsername(registerDto.getUsername())
                .setEmail(registerDto.getEmail())
                .setPassword(registerDto.getPassword())
                .build();

        AddNewUserResponse newUserResponse = userAccountServiceStub.addNewUser(userRequest);

        return newUserResponse != null && newUserResponse.getCreated();

    }
}
