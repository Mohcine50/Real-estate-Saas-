package com.shegami.authservice;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.shegami.authservice.config.RsaKeysConfig;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@SpringBootApplication
@AllArgsConstructor
@EnableConfigurationProperties(RsaKeysConfig.class)
@EnableDiscoveryClient
public class AuthServiceApplication {


	private RsaKeysConfig rsaKeysConfig;

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtEncoder JwtEncoder() {

		JWK jwk = new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

		return new NimbusJwtEncoder(jwkSource);

	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {

		var daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);

		return new ProviderManager(daoAuthenticationProvider);
	}






}
