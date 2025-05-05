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
        Project savedProject = projectService.createProject(projectDTO);

        ResponseDTO<Project> response = ResponseDTO.success(
                "Project created successfully",
                savedProject
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/new")
    public String showNewProjectForm() {
        return "projectForm";
    }
}
