package com.crg.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityService securityService;

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {

        List<com.crg.todo.security.entity.User>
                usersFromDatabase =
                securityService.findAllUsers();
        List<UserDetails> users = new ArrayList<>();

        for (com.crg.todo.security.entity.User userFromDatabase : usersFromDatabase) {
            UserDetails
                    user =
                    User.withUsername(userFromDatabase.getUsername())
                            .password(passwordEncoder.encode(userFromDatabase.getPassword()))
                            .roles(userFromDatabase.getRole())
                            .build();

            users.add(user);
        }
//        UserDetails
//                user =
//                User.withUsername("user")
//                        .password(passwordEncoder.encode("user"))
//                        .roles("USER")
//                        .build();
//        UserDetails
//                admin =
//                User.withUsername("admin")
//                        .password(passwordEncoder.encode("admin"))
//                        .roles("USER", "ADMIN")
//                        .build();
//        return new InMemoryUserDetailsManager(user, admin);
        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/healthcheck")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder
                encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
