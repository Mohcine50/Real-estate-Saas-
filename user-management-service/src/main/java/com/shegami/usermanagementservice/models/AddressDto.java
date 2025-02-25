package com.shegami.usermanagementservice.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {

    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

}
