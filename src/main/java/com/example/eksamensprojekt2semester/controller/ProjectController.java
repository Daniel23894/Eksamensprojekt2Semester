package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.dto.ProjectDTO;
import com.example.eksamensprojekt2semester.dto.SubprojectDTO;
import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.model.*;
import com.example.eksamensprojekt2semester.service.SubprojectService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.eksamensprojekt2semester.service.ProjectService;
import com.example.eksamensprojekt2semester.service.TaskService;
import com.example.eksamensprojekt2semester.service.TeamMemberService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/projects") //Angiver grund URL for alle endpoints i denne controller
public class ProjectController extends BaseController {


    private final ProjectService projectService;
    private final TaskService taskService;
    private final TeamMemberService teamMemberService;
    private final SubprojectService subprojectService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskService taskService, TeamMemberService teamMemberService, SubprojectService subprojectService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.teamMemberService = teamMemberService;
        this.subprojectService = subprojectService;
    }

    //Viser en liste med alle projekter, kræver login
    @GetMapping("/")
    public String showAllProjects(HttpSession session, Model model) {
        if(session.getAttribute("loggedInUser") == null){
            return "redirect:/login"; //Omdiriger til login
        }
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects); //Tilføjer listen til modellen
        return "projects/overview";
    }

    // Viser formular til at oprette et nyt projekt
    @GetMapping("/new")
    public String showNewProjectForm(Model model) {
        model.addAttribute("projectDTO", new ProjectDTO()); // Tilføjer tom DTO til formularen
        model.addAttribute("statuses", StateStatus.values()); // Tilføjer alle mulige statusser til formularen
        return "projectForm"; // Returns the project creation form
    }

    // Gemmer et nyt projekt baseret projectForm
    @PostMapping("/new")
    public String createProject(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO,
                                BindingResult bindingResult, Model model) { /** BindingResult = captures any validation errors so we can check them **/

        /** If validation errors exist, return the form again with error messages **/
        if (bindingResult.hasErrors()) {
            return "projectForm"; // Return to form view
        }

        projectService.createProject(projectDTO); // Calls the service to create the project
        return "redirect:/projects/overview"; // Redirects to the project list after creation
    }


    /** Displays an overview of projects based on user preferences (search and filter).
        Only adjusts what is visible — no data is modified, so no POST method is needed.**/
    @GetMapping("/overview")
    public String showProjectOverview(HttpSession session,
                                      Model model,
                                      /** required = false: Makes search and status optional, so we don´t get a null value and error if user don't specify them  **/
                                      @RequestParam(required = false) String search,
                                      @RequestParam(required = false) StateStatus status) {

        TeamMember member = (TeamMember) session.getAttribute("loggedInUser");
        if (member == null) {
            return "redirect:/login";
        }

        List<ProjectDTO> projects;

        /** Retrieve all filtered or none filtered projects**/
        if(search != null && !search.isEmpty() || status != null) {
            projects = projectService.findProjectsBySearchAndStatus(search,status);
        } else {
            projects = projectService.getAllProjectsDTO();
        }

        /** For each project we calculate the completion% and the amount of team members **/
        for (ProjectDTO project : projects){
            int completionPercentage = taskService.calculateProjectCompletion(project.getId());
            project.setCompletionPercentage(completionPercentage);

            int teamMemberCount = teamMemberService.getTeamMemberCountByProjectId(project.getId());
            project.setTeamMemberCount(teamMemberCount);
        }


        model.addAttribute("member", member);    /** Adds retrieved from the session team member,so the HTML page can access and display user-specific data **/
        model.addAttribute("stateStatuses", StateStatus.values());   /** Adds all possible status values  **/
        model.addAttribute("projects", projects);    /** Adds list of projects (filtered or not, depending on the search)  **/

        return "overview_of_projects";
    }

        /** Displays the details of a single project based on its id **/
    @GetMapping("/details/{id}")
    public String showProjectDetails(@PathVariable int id, Model model) {

        /** Retrieve the project from the database based on the ID **/
        ProjectDTO project = projectService.getProjectDTOById(id);

        /** If the project does not exist, redirect to the overview page **/
        if (project == null) {
            return "redirect:/projects/overview";
        }

        /** Calculate project statistics **/
        int completionPercentage = taskService.calculateProjectCompletion(id);
        int totalTasks = taskService.getTotalTaskCount(id);
        int completedTasks = taskService.getCompletedTaskCount(id);
        List<TeamMember> teamMembers = teamMemberService.getTeamMembersByProjectId(id);

        /** Update the DTO with the dynamically calculated statistics, to avoid lack the correct values for those statistics since they may change **/
        project.setCompletionPercentage(completionPercentage);
        project.setTotalTasks(totalTasks);
        project.setCompletedTasks(completedTasks);

        /** Add subprojects with total and remaining hours **/
        List<Subproject> subprojects = subprojectService.findByProjectId(id);
        for (Subproject subproject : subprojects) {
            BigDecimal totalHours = subprojectService.calculateTotalHours(subproject.getId());
            BigDecimal remainingHours = subprojectService.calculateRemainingHours(subproject.getId());
            subproject.setTotalHours(totalHours);
            subproject.setRemainingHours(remainingHours);
        }

        /** Add the project statistics,team members data and subproject statistics to the model **/
        model.addAttribute("project", project);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("subprojects", subprojects);

        /**  Return projects containment with updated details*/
        return "project_details";
    }

        /** The edit form for a specific project based on its id **/
        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable("id") int id, Model model) {
            ProjectDTO projectDTO = projectService.getProjectDTOById(id);

            if (projectDTO == null) {
                model.addAttribute("errorMessage", "Projekt ikke fundet");
                return "error/error"; /** Redirect to a general error page **/
            }

            model.addAttribute("projectDTO", projectDTO);
            model.addAttribute("allStatuses", StateStatus.values());
            return "edit_project";
        }

    @PostMapping("/edit/{id}")
    public String updateProject(@PathVariable int id, @ModelAttribute("projectDTO") ProjectDTO projectDTO, Model model) {

        /** Form does not include the id field because users shouldn’t edit IDs.
         *  So we must set it ourselves because update logic needs to know the ID to update the right project.**/
        projectDTO.setId(id);

        try {
            /** Convert DTO to Project for use in our domain-driven logic **/
            Project project = projectService.convertToProject(projectDTO);

            projectService.updateProject(project);
            return "redirect:/projects/overview";
        } catch (ProjectNotFoundException e) {
            model.addAttribute("errorMessage", "Projekt opdatering fejlede: " + e.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable int id) {
        try {
            projectService.deleteProjectById(id);
        } catch (ProjectNotFoundException e) {
            return "redirect:/projects/overview?error=notfound";
        }
        return "redirect:/projects/overview";
    }
    @GetMapping("/project/{id}")
    public String viewProject(@PathVariable("id") int id, Model model) {
        try {
            Project project = projectService.getProjectById(id);
            List<SubprojectDTO> subprojects = (List<SubprojectDTO>) subprojectService.getSubprojectById(id);

            model.addAttribute("project", project);
            model.addAttribute("subprojects", subprojects);

            return "project_details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Projektet blev ikke fundet: " + e.getMessage());
            return "error/error";
        }
    }
    @GetMapping("/create")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("projectDTO", new ProjectDTO());
        model.addAttribute("statuses", StateStatus.values());
        return "projectForm"; // assumes you have templates/projectForm.html
    }

}


