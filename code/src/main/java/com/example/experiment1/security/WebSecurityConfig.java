package com.example.experiment1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // A) Allow anyone to register a new user
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                //B)  Allow authenticated users to vote
                .requestMatchers(HttpMethod.POST, "/vote/**").authenticated()
                // C) secure poll creation for ADMIN's (only admins, not "NORMAL" users!)
                .requestMatchers(new AntPathRequestMatcher("/polls/**", HttpMethod.POST.name())).hasRole("ADMIN")
                // All other requests need authentication (for safety purposes)
                .anyRequest().authenticated()
            )
            // Use HTTP Basic Auth instead of formLogin
            .httpBasic(Customizer.withDefaults());

        return security.build();
    }    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Do not use in production !!!
	// Ignore requests to the H2 database (so it isnt exposed)
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }
}
