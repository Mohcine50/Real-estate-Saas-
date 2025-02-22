package com.shegami.authservice.controllers;


import com.shegami.authservice.exceptions.ApiRequestException;
import com.shegami.authservice.exceptions.NotFoundException;
import com.shegami.authservice.models.AccountDto;
import com.shegami.authservice.models.LoginDto;
import com.shegami.authservice.models.RegisterDto;
import com.shegami.authservice.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@Slf4j
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDto registerDto) {

        Map<String, String> map = new HashMap<>();

        AccountDto appUser = userService.findUserByEmail(registerDto.getEmail());
        log.info("Registering user: {}", appUser);
        if (appUser != null) {
            throw new ApiRequestException("Username already exists");
        }


        Boolean userRegistered = userService.registerNewUser(registerDto);

        if (!userRegistered) {
            map.put("Message", "REGISTER NOT COMPLETED");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        map.put("Message", "REGISTER SUCCESSFULLY");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {

        Map<String, String> map = new HashMap<>();
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet;
        String jwtAccessToken;


        AccountDto appUser = userService.findUserByEmail(loginDto.getEmail());
        if (appUser == null) {
            throw new NotFoundException("User Not Found");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

            jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(authentication.getName())
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .issuer("security-service")
                    .claim("scope", scope)
                    .build();
            jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
            map.put("Message", "Login Successfully");
            map.put("accessToken", jwtAccessToken);
        } catch (AuthenticationException exception) {
            map.put("Message", "Wrong Password");
            map.put("Error", exception.getMessage());
            return new ResponseEntity<>(map, HttpStatus.resolve(401));
        }

        Cookie cookie = new Cookie("authorization_token", jwtAccessToken);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
