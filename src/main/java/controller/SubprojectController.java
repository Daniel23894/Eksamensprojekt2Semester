package controller;
import exception.ProjectNotFoundException;
import model.Project;
import model.StateStatus;
import model.Subproject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import service.ProjectService;
import service.SubprojectService;

import java.util.List;

@Controller
public class SubprojectController {
    private final ProjectService projectService;
    private final SubprojectService subprojectService;
    private static  final Logger logger = LoggerFactory.getLogger(SubprojectController.class);

    /** Constructor for SubprojectController with Dependency Injection **/
    public SubprojectController(ProjectService projectService, SubprojectService subprojectService) {
        this.projectService = projectService;
        this.subprojectService = subprojectService;
    }

    @GetMapping("/projects/subprojects/create")
    public String showCreateSubprojectForm(Model model){
        List<Project> listOfProjects = projectService.getAllProjects(); /** Retrieve all existing projects **/
        Subproject aSubproject = new Subproject();                      /** Create a new Subproject instance **/
        aSubproject.setStatus(StateStatus.NOT_STARTED);                 /** Set the default status to NOT_STARTED **/

        model.addAttribute("projects", listOfProjects);                 /** Add the list of projects to the model **/
        model.addAttribute("subproject", aSubproject);                  /** Add the new Subproject instance to the model **/

        return "create_subproject";                                     /** Page for creating a new subproject **/
    }

    @PostMapping("/projects/subproject/create")
    public String createSubproject(@ModelAttribute("subproject")Subproject subproject, Model model){

        /** Validate the subproject object **/
        try{
            subprojectService.createSubproject(subproject); /** Call service to create a new subproject **/
            return "redirect:/projects/" + subproject.getProjectId(); /** Redirect to the projects details page after creation **/

        /** Handle case where parent project doesn't exist with custom exception for improved readability **/
        } catch (ProjectNotFoundException e) {
            model.addAttribute("errorMessage", "Det valgte projekt findes ikke.");
            model.addAttribute("projects", projectService.findAll());
            return "create_subproject";

        /** Generic fallback for unexpected errors **/
        } catch (Exception e) {
            logger.error("Fejl ved oprettelse af subprojekt: ", e);
            model.addAttribute("errorMessage", "Der opstod en uventet fejl. Prøv igen senere.");
            model.addAttribute("projects", projectService.findAll());
            return "create_subproject";
        }
    }
}
