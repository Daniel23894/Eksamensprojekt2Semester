package com.example.eksamensprojekt2semester.exception.handler;

import com.example.eksamensprojekt2semester.exception.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    // Specific handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    // Generic handler for all other exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        // Print to console (helpful for dev)
        e.printStackTrace();  // <--- REMOVE in production

        // Add details to model
        model.addAttribute("message", e.getMessage());
        model.addAttribute("stackTrace", e.getStackTrace());

        // Show a custom error page
        return "error/general"; // Create this view
    }
}
