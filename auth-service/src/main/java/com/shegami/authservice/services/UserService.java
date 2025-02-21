package com.shegami.authservice.services;

import com.shegami.authservice.models.AppUserDto;
import com.shegami.authservice.models.RegisterDto;

public interface UserService {
    AppUserDto findUserByUsername(String username);
    Boolean registerNewUser(RegisterDto registerDto);
}
