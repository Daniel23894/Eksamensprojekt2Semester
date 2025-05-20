package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.eksamensprojekt2semester.service.ProjectService;
import com.example.eksamensprojekt2semester.service.SubprojectService;
import com.example.eksamensprojekt2semester.service.TaskService;

import java.util.List;
import java.util.ArrayList;

@Controller
public class SubprojectController {
    private final ProjectService projectService;
    private final SubprojectService subprojectService;
    private final TaskService taskService;

    /** Set up logger from Logger interface using SLF4J to access logging methods.
     It provides clear error tracking and separates user-friendly error messages from developer diagnostics **/
    private static final Logger logger = LoggerFactory.getLogger(SubprojectController.class);

    /** Constructor for SubprojectController with Dependency Injection **/
    public SubprojectController(ProjectService projectService, SubprojectService subprojectService, TaskService taskService) {
        this.projectService = projectService;
        this.subprojectService = subprojectService;
        this.taskService = taskService;
    }

    // FIXED: Use consistent URL pattern - /projects/subprojects/create for both GET and POST
    @GetMapping("/projects/subprojects/create")
    public String showCreateSubprojectForm(@RequestParam(required = false) Integer projectId, Model model){
        List<Project> listOfProjects = projectService.getAllProjects(); /** Retrieve all existing projects **/
        Subproject aSubproject = new Subproject();                      /** Create a new Subproject instance **/
        aSubproject.setStatus(StateStatus.NOT_STARTED);                 /** Set the default status to NOT_STARTED **/

        // If projectId is provided, pre-select it
        if (projectId != null) {
            aSubproject.setProjectId(projectId);
        }

        model.addAttribute("projects", listOfProjects);                 /** Add the list of projects to the model **/
        model.addAttribute("subproject", aSubproject);                  /** Add the new Subproject instance to the model **/

        return "create_subproject";                                     /** Page for creating a new subproject **/
    }

    @PostMapping("/projects/subprojects/create")
    public String createSubproject(@ModelAttribute("subproject")Subproject subproject, Model model){

        /** Validate the subproject object **/
        try{
            subprojectService.createSubproject(subproject); /** Call service to create a new subproject **/
            return "redirect:/projects/details/" + subproject.getProjectId(); /** Redirect to the projects details page after creation **/

            /** Handle case where parent project doesn't exist with custom exception for improved readability **/
        } catch (ProjectNotFoundException e) {
            model.addAttribute("errorMessage", "Det valgte projekt findes ikke.");
            model.addAttribute("projects", projectService.getAllProjects());
            return "create_subproject";

            /** Generic fallback for unexpected errors **/
        } catch (Exception e) {
            e.printStackTrace(); // <- Dette vil vise stack trace direkte i konsollen
            logger.error("Fejl ved oprettelse af subprojekt: ", e);
            model.addAttribute("errorMessage", "Der opstod en uventet fejl. Prøv igen senere.");
            model.addAttribute("projects", projectService.getAllProjects());
            return "create_subproject";
        }
    }

    @GetMapping("/subprojects/{id}")
    public String viewSubproject(@PathVariable("id") int id, Model model) {
        try {
            // Get the subproject details
            Subproject subproject = subprojectService.getSubprojectById(id);

            // Get tasks associated with this subproject
            List<Task> tasks = taskService.getTasksBySubprojectId(id);

            // Add the subproject and tasks to the model
            model.addAttribute("subproject", subproject);
            model.addAttribute("tasks", tasks);

            return "view_subproject"; // Return dedicated subproject details view
        } catch (SubprojectNotFoundException e) {
            logger.error("Subproject not found with ID: " + id, e);
            model.addAttribute("errorMessage", "Subprojektet blev ikke fundet.");
            return "error/error"; // Redirect to an error page
        } catch (Exception e) {
            logger.error("Error retrieving subproject with ID: " + id, e);
            model.addAttribute("errorMessage", "Der opstod en fejl ved indlæsning af subprojektet.");
            return "error/error";
        }
    }
}