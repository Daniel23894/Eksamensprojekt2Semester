package controller;

import dto.ProjectDTO;
import dto.ResponseDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import model.Project;
import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ProjectService;

import java.util.List;

@Controller
@RequestMapping("projects") //Angiver grund URL for alle endpoints i denne controller
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //Tilføjer rolleinformation til alle views
    @ModelAttribute
    public void addRoleAttributesToModel(HttpSession session, Model model) {
        TeamMember teamMember = (TeamMember) session.getAttribute("teamMember");
        if (teamMember != null) {
            model.addAttribute("isAdmin", teamMember.isAdmin());
            model.addAttribute("isDeveloper", teamMember.isDeveloper());
            model.addAttribute("isPO", teamMember.isPO());
      }
    }

    //Viser en liste med alle projekter, kræver login
    @GetMapping
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
    }
