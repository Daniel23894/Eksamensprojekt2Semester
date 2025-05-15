package com.example.eksamensprojekt2semester.exception.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    // This handles all Exception types
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());  // Adds error message to model
        return "error";  // Returns the error view (error.html)
    }
}
