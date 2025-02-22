package com.shegami.authservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {

    @NotNull(message = "First name must not be null")
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotNull(message = "Phone number must not be null")
    @NotBlank(message = "Phone number must not be blank")
    private String phoneNumber;

    private AddressDto address;


}
