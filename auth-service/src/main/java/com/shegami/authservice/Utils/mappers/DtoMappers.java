package com.shegami.authservice.Utils.mappers;


import com.shegami.authservice.Role;
import com.shegami.authservice.models.RoleDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMappers {

    public static Collection<RoleDto> grpcRoleListToRoleDtoCollection(List<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDto.Builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());

    }

}
