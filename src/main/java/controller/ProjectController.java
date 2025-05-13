package controller;

import dto.ProjectDTO;
import dto.ResponseDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import model.Project;
import model.Role;
import model.StateStatus;
import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ProjectService;
import service.TaskService;
import service.TeamMemberService;

import java.util.List;

@Controller
@RequestMapping("/projects") //Angiver grund (base) URL for alle endpoints i denne controller
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final TeamMemberService teamMemberService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskService taskService, TeamMemberService teamMemberService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.teamMemberService = teamMemberService;
    }

    /**  ModelAttribute, makes so that is automatically invoked before every controller method. **/
    @ModelAttribute
    public void addRoleAttributesToModel(HttpSession session, Model model) {
        TeamMember teamMember = (TeamMember) session.getAttribute("teamMember");
        if (teamMember != null) {
            /** == to compare enum constants **/
            model.addAttribute("isAdmin", teamMember.getRole() == Role.ADMIN);
            model.addAttribute("isDeveloper", teamMember.getRole() == Role.DEVELOPER);
            model.addAttribute("isPO", teamMember.getRole() == Role.PRODUCT_OWNER);
        }
    }


    //Viser en liste med alle projekter, kræver login
    @GetMapping("/")
    public String showAllProjects(HttpSession session, Model model) {
        if(session.getAttribute("employee") == null){
            return "redirect:/login"; //Omdiriger til login
        }
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects); //Tilføjer listen til modellen
        return "projectsList";
    }

    // Viser formular til at oprette et nyt projekt
    @GetMapping("/new")
    public String showNewProjectForm(HttpSession session, Model model) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        }
        model.addAttribute("projectDTO", new ProjectDTO()); // Tilføjer tom DTO til formularen
        return "projectForm";
    }

    // Gemmer et nyt projekt baseret projectForm
    @PostMapping
    public String createProject(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO) {
        projectService.createProject(projectDTO); // Kalder service til at oprette projekt
        return "redirect:/projectList"; // Omdiriger til projektlisten
        }

    /** Displays an overview of projects based on user preferences (search and filter).
        Only adjusts what is visible — no data is modified, so no POST method is needed.**/
    @GetMapping("/overview")
    public String showProjectOverview(Model model,
                                       /** required = false: Makes search and status optional, so we don´t get a null value and error if user don't specify them  **/
                                      @RequestParam(required = false) String search,
                                      @RequestParam(required = false) StateStatus status){

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

        /** Adds all possible status values, and list of projects (filtered or not, depending on the search) **/
        model.addAttribute("stateStatuses", StateStatus.values());
        model.addAttribute("projects", projects);

        return "overview_of_projects";
    }

    /** Displays the details of a single project based on its id **/
    @GetMapping("/details/{id}")
    public String showProjectDetails(@PathVariable int id, Model model) {

        /** Retrieve the project from the database based on the ID **/
        ProjectDTO project = projectService.getProjectDTOById(id);

        /** If the project does not exist, redirect to the overview page **/
        if (project == null) {
            return "redirect:/project/overview";
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

        /** Add the project statistiks and team members data to the model **/
        model.addAttribute("project", project);
        model.addAttribute("teamMembers", teamMembers);

        /**  Return projects containment with updated details*/
        return "project_details";
    }
}
