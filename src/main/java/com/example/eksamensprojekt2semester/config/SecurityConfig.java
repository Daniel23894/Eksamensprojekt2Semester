package com.example.eksamensprojekt2semester.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** BCryptPasswordEncoder is a utility class that helps hash passwords.
 Instead of making new instances all over the code (new BCryptPasswordEncoder()), declare it once as a bean. **/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class SecurityConfig {

    /** Whenever you need it, you just inject the shared bean.
     This promotes reuse, consistency, and easier testing.**/
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}



