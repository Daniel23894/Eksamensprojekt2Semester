package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

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

        // Hardcoded check: admin / 1234
        if ("admin".equals(username) && "1234".equals(password)) {
            // Redirect to overview page with a query parameter indicating that user is logged in
            return "redirect:/projects/overview?loggedIn=true";
        }

        // Redirect to login page with an error if login is incorrect
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login"; // Simply redirect to login page on logout
    }
}
