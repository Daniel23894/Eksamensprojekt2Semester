package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.dto.ProjectDTO;
import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.model.StateStatus;

import java.util.List;

public interface ProjectService {

    Project createProject(ProjectDTO projectDTO);
    List<Project> getAllProjects();
    ProjectDTO getProjectDTOById(int id);
    List<ProjectDTO> getAllProjectsDTO();
    List<ProjectDTO> findProjectsBySearchAndStatus(String search, StateStatus status);
    Project getProjectById(int id);
    boolean existsById(int id);
    void updateProject(Project project);
    Project convertToProject(ProjectDTO dto);
    void deleteProjectById(int id) throws ProjectNotFoundException;



}
