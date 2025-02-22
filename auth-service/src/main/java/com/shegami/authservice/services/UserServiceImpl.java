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

        log.info("Registering new user: {}", registerDto.getProfile());
        Address address = registerDto.getProfile().getAddress() != null ? Address.newBuilder()
                .setStreet(registerDto.getProfile().getAddress().getStreet())
                .setCity(registerDto.getProfile().getAddress().getCity())
                .setState(registerDto.getProfile().getAddress().getState())
                .setZip(registerDto.getProfile().getAddress().getZip())
                .setCountry(registerDto.getProfile().getAddress().getCountry())
                .build() : null;

        AddNewUserRequest userRequest = AddNewUserRequest.newBuilder()
                .setEmail(registerDto.getEmail())
                .setPassword(registerDto.getPassword())
                .setAddress(address)
                .setFirstName(registerDto.getProfile().getFirstName())
                .setLastName(registerDto.getProfile().getLastName())
                .setPhoneNumber(registerDto.getProfile().getPhoneNumber())
                .build();

        AddNewUserResponse newUserResponse = userAccountServiceStub.addNewUser(userRequest);
        log.info("Registering new user: \n {}", newUserResponse);

        return newUserResponse != null && newUserResponse.getCreated();

    }
}
