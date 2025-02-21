package com.shegami.usermanagementservice;


import com.shegami.usermanagementservice.entities.Role;
import com.shegami.usermanagementservice.services.rest.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    CommandLineRunner commandLineRunner(AccountService accountService) {

        return args -> {

            accountService.addNewRole(Role.builder()
                    .id(null)
                    .name("USER")
                    .build());
            accountService.addNewRole(Role.builder()
                    .id(null)
                    .name("ADMIN")
                    .build());
            accountService.addNewRole(Role.builder()
                    .id(null)
                    .name("MANAGER")
                    .build());
        };
    }


}
