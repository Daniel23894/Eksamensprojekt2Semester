package controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dto.ProjectDTO;
import dto.ResponseDTO;
import model.Project;
import service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Project>> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        if (projectDTO.getId() != null) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error("Project ID should not be provided for new projects"));
        }

        // Create the project
        // Assuming ProjectService has a method to create a project
        // and it handles the business logic
        // including setting default values for status and budget
        // if not provided in the request
        if (projectDTO.getStatus() != null || projectDTO.getBudget() != null) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error("Project status and budget should not be provided for new projects"));
        }

        // Create the project using the service
        Project savedProject = projectService.createProject(projectDTO);

        ResponseDTO<Project> response = ResponseDTO.success(
                "Project created successfully",
                savedProject );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/new")
    public String showNewProjectForm() {
        return "projectForm";
    }
}
