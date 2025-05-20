package com.example.eksamensprojekt2semester.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.eksamensprojekt2semester.model.StateStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjectDTO {
    private int id;

    /** Using Bean Validation annotations, to ensure that object fields meet specific constraints **/
    @NotBlank(message = "Project name is required")
    private String name;

    @Size(max = 1000, message = "Beskrivelsen må højst være 1000 tegn")
    private String description;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualEndDate;

    @NotNull(message = "Projektstatus er påkrævet")
    private StateStatus status; // Enum for the project status

    @NotNull(message = "Budget er påkrævet")
    @Min(value = 0, message = "Budget kan ikke være negativt")
    private BigDecimal budget;

    /** For task 8.2 Fetching of project status, progress, time stats, team info **/
    @Min(value = 0, message = "Fuldførelsesprocenten skal være mindst 0")
    @Max(value = 100, message = "Fuldførelsesprocenten kan ikke overstige 100")
    private int completionPercentage;

    @Min(value = 0, message = "Antallet af teammedlemmer skal være mindst 0")
    private int teamMemberCount;

    @Min(value = 0, message = "Antallet af opgaver kan ikke være negativt")
    private int totalTasks;

    @Min(value = 0, message = "Antallet af fuldførte opgaver kan ikke være negativt")
    private int completedTasks;

    // Added subprojects field to maintain relationship with subprojects
    private List<SubprojectDTO> subprojects;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /** Convert the LocalDate (data type that we choose for dates) to a String in the format yyyy-mm-dd,
     *  which is what the HTML and Thymeleaf requires to display dates properly **/
    public String getStartDateFormatted() {
        return startDate != null ? startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : ""; /** Method follows ISO standard for dates: YYYY-MM-DD **/
    }

    /**  If no value found we return a empty string: "" to avoid errors due to null or improperly formatted value
     *   that won´t show up in front end due to miss matched data types cause value inputs require a string in Thymeleaf **/
    public String getEndDateFormatted() {
        return endDate != null ? endDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }


    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDate getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public StateStatus getStatus() {
        return status;
    }

    public void setStatus(StateStatus status) {
        this.status = status;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public int getTeamMemberCount() {
        return teamMemberCount;
    }

    public void setTeamMemberCount(int teamMemberCount) {
        this.teamMemberCount = teamMemberCount;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    // New getters and setters for subprojects
    public List<SubprojectDTO> getSubprojects() {
        return subprojects;
    }

    public void setSubprojects(List<SubprojectDTO> subprojects) {
        this.subprojects = subprojects;
    }
}