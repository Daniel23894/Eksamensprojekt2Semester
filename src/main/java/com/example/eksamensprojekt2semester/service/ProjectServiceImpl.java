package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.dto.ProjectDTO;
import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.model.StateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.ProjectRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(projectDTO.getStatus());
        project.setBudget(projectDTO.getBudget());

        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(int id) {
        Project project = projectRepository.findById(id); /** Retrieves project by ID from database**/
        if (project == null) {
            throw new ProjectNotFoundException("Project med ID " + id + " blev ikke fundet");
        }
        return project;
    }

    @Override
    public ProjectDTO getProjectDTOById(int id) {
        Project project = projectRepository.findById(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project med ID " + id + " blev ikke fundet");
        }
        /** Pass a list containing the single project to the method that expects a list **/
        return convertToProjectDTOList(List.of(project)).get(0);  /** Convert to list and return the first element to match expected paramter type in the method  **/
    }


    @Override
    public boolean existsById(int id) {
        return projectRepository.existsById(id);
    }

    @Override
    public List<ProjectDTO> getAllProjectsDTO(){
        List<Project> projects = projectRepository.findAll(); /** Retrieves all projects from database**/
        return convertToProjectDTOList(projects); /** Convert to DTOs and return **/
    }


    @Override
    public List<ProjectDTO> findProjectsBySearchAndStatus(String search, StateStatus status) {
        List<Project> projects;

        boolean hasSearch = search != null && !search.trim().isEmpty(); /** Check if the search string is not null and not just whitespace **/
        boolean hasStatus = status != null; /** Check if a valid status was provided **/

        if (hasSearch && hasStatus) {
            /** Filter by both project name and status **/
            projects = projectRepository.findByNameContainingAndStatus(search.trim(), status);

        } else if (hasSearch) {
            /** Filter by project name only **/
            projects = projectRepository.findByNameContaining(search.trim());

        } else if (hasStatus) {
            /** Filter by status only **/
            projects = projectRepository.findByStatus(status);

        } else {
            /** No filters provided, return all projects **/
            projects = projectRepository.findAll();
        }

        /** Project into ProjectDTO objects**/
        return convertToProjectDTOList(projects);
    }

    /** Converts a list of Project objects to a list of ProjectDTO objects **/
    private List<ProjectDTO> convertToProjectDTOList(List<Project> projects) {
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (Project project : projects) {
            ProjectDTO dto = new ProjectDTO();

            /** Map the fields from Project to ProjectDTO **/
            dto.setId(project.getProjectId());
            dto.setName(project.getName());
            dto.setDescription(project.getDescription());
            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());
            dto.setStatus(project.getStatus());
            dto.setActualStartDate(project.getActualStartDate());
            dto.setActualEndDate(project.getActualEndDate());
            dto.setBudget(project.getBudget());
            dto.setCompletionPercentage(project.getCompletionPercentage());

            projectDTOs.add(dto);
        }

        return projectDTOs;
    }

}
