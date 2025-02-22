package com.shegami.authservice.models;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AccountTypeDto {
    String id;
    String name;
}
