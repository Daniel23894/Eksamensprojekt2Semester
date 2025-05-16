package com.example.eksamensprojekt2semester.config;

import com.example.eksamensprojekt2semester.service.TeamMemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminCreation {

    @Bean
    public CommandLineRunner createAdmin(TeamMemberService teamMemberService) {
        return args -> {
            teamMemberService.createAdminIfMissing();
        };
    }
}
