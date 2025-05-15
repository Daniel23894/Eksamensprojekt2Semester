package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import com.example.eksamensprojekt2semester.repository.TeamMemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final TeamMemberRepository teamMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginController(TeamMemberRepository teamMemberRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.teamMemberRepository = teamMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login"; // Return login page
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password) {

        /** Look up the user by email (username) **/
        TeamMember member = teamMemberRepository.findByEmail(username);

        /** Check if user exists and password matches**/
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {

            /** Login successful, redirect to overview **/
            return "redirect:/projects/overview?loggedIn=true";
        }
        /** Login failed **/
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login"; // Simply redirect to login page on logout
    }
}
