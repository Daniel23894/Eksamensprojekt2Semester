package com.example.eksamensprojekt2semester.exception.handler;

import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.exception.ResourceNotFoundException;
import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    // Specific handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404"; // Make sure this matches your template path
    }

    // Specific handler for ProjectNotFoundException
    @ExceptionHandler(ProjectNotFoundException.class)
    public String handleProjectNotFound(ProjectNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404"; // Make sure this matches your template path
    }

    // Specific handler for SubprojectNotFoundException
    @ExceptionHandler(SubprojectNotFoundException.class)
    public String handleSubprojectNotFound(SubprojectNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404"; // Make sure this matches your template path
    }

    // Generic handler for all other exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        // Add details to model
        model.addAttribute("message", e.getMessage());
        model.addAttribute("stackTrace", e.getStackTrace());

        // Show a custom error page
        return "error/error"; // Make sure this matches your template path
    }
}