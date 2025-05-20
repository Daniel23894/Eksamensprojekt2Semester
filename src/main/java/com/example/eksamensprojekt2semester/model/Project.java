package com.example.eksamensprojekt2semester.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project {
    private Integer projectId;                   // Changed from int to Integer
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private StateStatus status;
    private BigDecimal budget;
    private Integer completionPercentage;       // Changed from int to Integer

    /** No-args constructor **/
    public Project() {
    }

    /** Constructor with all fields except 'id' **/
    public Project(String name, Integer completionPercentage, BigDecimal budget, StateStatus status, LocalDate endDate, LocalDate startDate, LocalDate actualStartDate, LocalDate actualEndDate,  String description) {
        this.name = name;
        this.completionPercentage = completionPercentage;
        this.budget = budget;
        this.status = status;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.startDate = startDate;
        this.description = description;
    }

    /** Overloaded constructor with optional fields **/
    public Project(String name, Integer completionPercentage, StateStatus status) {
        this.name = name;
        this.status = status;
        this.completionPercentage = completionPercentage;
        this.description = null;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusMonths(6);
        this.actualStartDate = null;
        this.actualEndDate = null;
        this.budget = BigDecimal.ZERO;
    }

    /** Getter methods **/
    public Integer getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public LocalDate getActualEndDate() {
        return actualEndDate;
    }

    public StateStatus getStatus() {
        return status;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    /** Setter methods with validation **/
    public void setProjectId(Integer projectId) {
        if (projectId != null && projectId < 0) {
            throw new IllegalArgumentException("Project ID kan ikke være negativt.");
        }
        this.projectId = projectId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Navnet kan ikke være tomt.");
        }
        if (name.length() < 4) {
            throw new IllegalArgumentException("Navnet skal være mindst 4 tegn.");
        }
        if (!Character.isUpperCase(name.charAt(0))) {
            throw new IllegalArgumentException("Navnet skal starte med stort bogstav.");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            this.description = "Ingen beskrivelse angivet";
        }
        else if (description.length() > 100) {
            throw new IllegalArgumentException("Beskrivelsen må ikke være længere end 100 tegn.");
        } else {
            this.description = description;
        }
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Startdato må ikke være null.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null) {
            if (startDate == null) {
                throw new IllegalArgumentException("Startdato skal sættes før slutdato.");
            }
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("Slutdato kan ikke være før startdato.");
            }
        }
        this.endDate = endDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        if (actualStartDate != null && actualStartDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Actual start date cannot be before the planned start date.");
        }
        this.actualStartDate = actualStartDate;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        if (actualEndDate != null && actualEndDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Actual end date cannot be before the planned start date.");
        }
        if (actualEndDate != null && actualStartDate != null && actualEndDate.isBefore(actualStartDate)) {
            throw new IllegalArgumentException("Actual end date cannot be before the actual start date.");
        }
        this.actualEndDate = actualEndDate;
    }

    public void setStatus(StateStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status må ikke være null.");
        }
        this.status = status;
    }

    public void setBudget(BigDecimal budget) {
        if (budget != null && budget.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget kan ikke være negativt.");
        }
        this.budget = budget != null ? budget : BigDecimal.ZERO;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        if (completionPercentage != null && (completionPercentage < 0 || completionPercentage > 100)) {
            throw new IllegalArgumentException("Fuldførelsesprocenten skal være mellem 0 og 100.");
        }
        this.completionPercentage = completionPercentage;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", actualStartDate=" + actualStartDate +
                ", actualEndDate=" + actualEndDate +
                ", status=" + status +
                ", budget=" + budget +
                ", completionPercentage=" + completionPercentage +
                '}';
    }
}
