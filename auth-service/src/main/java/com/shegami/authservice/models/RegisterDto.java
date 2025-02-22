package com.shegami.authservice.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;


@Data
public class RegisterDto {

    @NotBlank(message = "Email is empty")
    @NotNull(message = "Email is empty")
    @Email(message = "Not a valid Email")
    private String email;

    @NotNull(message = "password is empty")
    @NotBlank(message = "Password is empty")
    @Size(min = 8, message = "Weak Password")
    private String password;

}
