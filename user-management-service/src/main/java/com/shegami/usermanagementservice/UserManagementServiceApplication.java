package com.shegami.usermanagementservice;


import com.shegami.usermanagementservice.entities.AccountType;
import com.shegami.usermanagementservice.models.Type;
import com.shegami.usermanagementservice.services.rest.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableDiscoveryClient
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService) {

        return args -> {

            accountService.addNewType(AccountType.builder()
                    .id(null)
                    .name(Type.ADMIN.name())
                    .build());
            accountService.addNewType(AccountType.builder()
                    .id(null)
                    .name(Type.AGENCY_EMPLOYEE.name())
                    .build());
            accountService.addNewType(AccountType.builder()
                    .id(null)
                    .name(Type.INDIVIDUAL.name())
                    .build());
            accountService.addNewType(AccountType.builder()
                    .id(null)
                    .name(Type.AGENCY_MARKETER.name())
                    .build());
            accountService.addNewType(AccountType.builder()
                    .id(null)
                    .name(Type.INDEPENDENT_MARKETER.name())
                    .build());
        };
    }


}
