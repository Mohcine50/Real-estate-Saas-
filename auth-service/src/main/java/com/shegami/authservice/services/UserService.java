package com.shegami.authservice.services;

import com.shegami.authservice.models.AccountDto;
import com.shegami.authservice.models.RegisterDto;

public interface UserService {
    AccountDto findUserByEmail(String email);
    Boolean registerNewUser(RegisterDto registerDto);
}
