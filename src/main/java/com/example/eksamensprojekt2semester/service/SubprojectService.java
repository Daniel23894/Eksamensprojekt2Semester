package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import com.example.eksamensprojekt2semester.model.Subproject;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SubprojectService {
    private final SubprojectRepository subprojectRepository;
    private final ProjectService projectService;
    private final TaskService taskService;

    /** Constructor injection for SubprojectRepository **/
    public SubprojectService(SubprojectRepository subprojectRepository, ProjectService projectService, TaskService taskService){
        this.subprojectRepository = subprojectRepository;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    /** CREATE - method to create a new subproject **/
    @Transactional
    public void createSubproject(Subproject subproject){

        /** Check if the subproject name is null or empty **/
        if (subproject.getName() == null || subproject.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subprojekt navn er påkrævet");
        }

        /** Negative or zero IDs are invalid **/
        if (subproject.getProjectId() <= 0) {
            throw new IllegalArgumentException("Valg af overordnet projekt er påkrævet");
        }

        /** Check if the project with the given projectId exists in the database **/
        if (!projectService.existsById(subproject.getProjectId())) {
            throw new IllegalArgumentException("Det valgte projekt findes ikke");
        }

        /** Check if the completion percentage is within the valid range of 0 to 100. **/
        Integer completionObj = subproject.getCompletionPercentage();
        int completion = (completionObj != null) ? completionObj : 0;
        if (completion < 0 || completion > 100) {
            throw new IllegalArgumentException("Færdiggørelsesprocent skal være mellem 0 og 100");
        }
        /** Set default values for status and completion percentage if not provided **/
        if (completionObj == null) {
            subproject.setCompletionPercentage(0);
        }


        /** Save the subproject if all validations pass **/
        subprojectRepository.save(subproject);
    }

    /** READ - find a specific subprojekt **/
    public Subproject findById(int id) {
        return subprojectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subprojekt ikke fundet"));
    }

    /** READ - find subprojects by project ID **/
    public List<Subproject> findByProjectId(int projectId) {
        return subprojectRepository.findByProjectId(projectId);
    }

    /** READ - find all subprojects **/
    public List<Subproject> findAll() {
        return subprojectRepository.findAll();
    }


    /** UPDATE - update a existing subproject **/
    @Transactional
    public void updateSubproject(Subproject subproject) {

        /** Convert primitives to Integer objects for null checks **/
        Integer id = subproject.getId();
        Integer projectId = subproject.getProjectId();

        /** Check if subproject ID is null or doesn't exist in the repository **/
        if (id == null || !subprojectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subprojektet findes ikke");
        }

        /** Validate projectId **/
        if (projectId == null) {
            throw new IllegalArgumentException("Valg af overordnet projekt er påkrævet");
        }

        /**  Check if the selected project exists **/
        if (!projectService.existsById(projectId)) {
            throw new IllegalArgumentException("Det valgte projekt findes ikke");
        }

        subprojectRepository.update(subproject);
    }


    /** DELETE - delete a subproject **/
    @Transactional
    public void deleteById(int id) {
        subprojectRepository.deleteById(id);
    }

    public BigDecimal calculateTotalHours(int subprojectId) {
        return taskService.calculateTotalHoursBySubproject(subprojectId);
    }

    public BigDecimal calculateRemainingHours(int subprojectId) {
        return taskService.calculateRemainingHoursBySubproject(subprojectId);
    }

    public BigDecimal getTotalEstimatedHours(int subprojectId) {
        try {
            return taskService.calculateTotalHoursBySubproject(subprojectId);
        } catch (Exception e) {
            System.err.println("Error calculating total estimated hours: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getRemainingHours(int subprojectId) {
        try {
            return taskService.calculateRemainingHoursBySubproject(subprojectId);
        } catch (Exception e) {
            System.err.println("Error calculating remaining hours: " + e.getMessage());
            return BigDecimal.ZERO;
            }
        }
    // In SubprojectService class
    public Subproject getSubprojectById(int id) {
        Subproject subproject = subprojectRepository.getSubprojectById(id);
        if (subproject == null) {
            throw new SubprojectNotFoundException("Subproject with ID " + id + " was not found.");
        }
        return subproject;
    }
    @Transactional
    public void save(Subproject subproject) {
        if (subproject.getId() == null || !subprojectRepository.existsById(subproject.getId())) {
            // New subproject — create it
            createSubproject(subproject);
        } else {
            // Existing subproject — update it
            updateSubproject(subproject);
        }
    }
}