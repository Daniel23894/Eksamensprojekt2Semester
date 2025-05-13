package com.example.eksamensprojekt2semester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    // Test mapping for /test
    @GetMapping("/test")
    public String testPage() {
        // This should return a basic test page to verify the mapping is working
        return "test_page"; // Name of the Thymeleaf template that should be rendered
    }
}
