package service;

import dto.ProjectDTO;
import model.Project;
import model.StateStatus;

import java.util.List;

public interface ProjectService {

    Project createProject(ProjectDTO projectDTO);
    List<Project> getAllProjects();
    List<ProjectDTO> getAllProjectsDTO();
    List<ProjectDTO> findProjectsBySearchAndStatus(String search, StateStatus status);
    Project getProjectById(int id);
    boolean existsById(int id);
}
