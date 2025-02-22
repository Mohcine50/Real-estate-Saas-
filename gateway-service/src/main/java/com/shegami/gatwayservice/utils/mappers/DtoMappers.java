package com.shegami.gatwayservice.utils.mappers;

import com.shegami.gatewayservice.Type;
import com.shegami.gatwayservice.models.AccountTypeDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMappers {

    public static Collection<AccountTypeDto> grpcRoleListToRoleDtoCollection(List<Type> roles) {
        return roles.stream()
                .map(type -> new AccountTypeDto.Builder()
                        .id(type.getId())
                        .name(type.getName())
                        .build())
                .collect(Collectors.toList());

    }

}
