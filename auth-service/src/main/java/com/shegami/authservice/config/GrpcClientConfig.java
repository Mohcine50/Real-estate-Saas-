package com.shegami.authservice.config;

import com.shegami.authservice.UserAccountServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel userServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserAccountServiceGrpc.UserAccountServiceBlockingStub userAccountServiceStub(ManagedChannel userServiceChannel) {
        return UserAccountServiceGrpc.newBlockingStub(userServiceChannel);
    }

}
