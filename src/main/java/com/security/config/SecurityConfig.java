package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager=true)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .authorizeExchange(auth -> auth
                        .anyExchange().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("sam")
                .password(passwordEncoder().encode("psycho"))
                .roles("ultimate")
                .build();
        UserDetails user2 = User.builder()
                .username("raj")
                .password(passwordEncoder().encode("sekhar"))
                .roles("premium")
                .build();
        UserDetails user3 = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("free-tier")
                .build();
        return new MapReactiveUserDetailsService(user1, user2, user3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
