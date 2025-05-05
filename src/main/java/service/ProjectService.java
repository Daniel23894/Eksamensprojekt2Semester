package service;

import dto.ProjectDTO;
import model.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDTO projectDTO);
    List<Project> getAllProjects();
    List<Project> findAll();
    List<Project> existByID();
}
