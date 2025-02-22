package com.shegami.authservice.Utils.mappers;


import com.shegami.authservice.models.AccountTypeDto;
import com.shegami.authservice.models.Type;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMappers {

    public static Collection<AccountTypeDto> grpcRoleListToRoleDtoCollection(List<com.shegami.authservice.Type> types) {
        return types.stream()
                .map(type -> AccountTypeDto.builder()
                        .id(type.getId())
                        .name(type.getName())
                        .build())
                .collect(Collectors.toList());

    }

}
