package com.hrproject.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CommentServiceSecurityConfig {

    private static final String[] WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/activate_status",
            "/api/v1/auth/**",
            "/api/v1/auth/activation",
            "/api/v1/auth/register_with_rabbitmq",
            "/**",
            "**",
            "localhost:3000/**"
    };
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();

        httpSecurity.authorizeRequests().antMatchers(WHITELIST).permitAll().anyRequest().authenticated();

//        httpSecurity.authorizeRequests().antMatchers("/find_all").hasAuthority("ADMIN");
//
//        httpSecurity.authorizeRequests().antMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated().anyRequest().permitAll();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}