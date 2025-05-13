package com.example.eksamensprojekt2semester.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Task {
    private int id;
    private String name;
    private LocalDate deadline;
    private BigDecimal estimatedHours;
    private BigDecimal usedHours;
    private int completionPercentage;
    private StateStatus status;
    private int subprojectId;

    /** No-args constructor **/
    public Task() {
    }

    /** No-args constructor – required by frameworks and libraries like RowMapper **/
    public Task(String name, LocalDate deadline, BigDecimal estimatedHours, BigDecimal usedHours, int completionPercentage, StateStatus status, int subprojectId) {
        this.name = name;
        this.deadline = deadline;
        this.estimatedHours = estimatedHours;
        this.usedHours = usedHours;
        this.completionPercentage = completionPercentage;
        this.status = status;
        this.subprojectId = subprojectId;
    }

    /** Overloaded constructor with optional fields **/
    public Task(String name, LocalDate deadline, int subprojectId) {
        this.name = name;
        this.deadline = deadline;
        this.subprojectId = subprojectId;
        this.estimatedHours = BigDecimal.ZERO;  /** Default value **/
        this.usedHours = BigDecimal.ZERO;       /** Default value **/
        this.completionPercentage = 0;          /** Default value **/
        this.status = StateStatus.NOT_STARTED;  /** Default value **/
    }

    /** Getter methods **/
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public BigDecimal getUsedHours() {
        return usedHours;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public StateStatus getStatus() {
        return status;
    }

    public int getSubprojectId() {
        return subprojectId;
    }

    /** Setter methods **/
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Task ID kan ikke være negativt.");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Task navn kan ikke være null eller tomt.");
        }
        if (name.length() < 4) {
            throw new IllegalArgumentException("Task navn skal være mindst 4 tegn langt.");
        }
        this.name = name;
    }

    public void setDeadline(LocalDate deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("Task deadline kan ikke være null.");
        }
        /** Check om deadline er i fortiden **/
        if (deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Task deadline kan ikke være i fortiden.");
        }

        /** Check om deadline er for langt ude i fremtiden (max 1 måned frem) **/
        if (deadline.isAfter(LocalDate.now().plusMonths(1))) {
            throw new IllegalArgumentException("Task deadline kan ikke være mere end 1 måned frem i tiden.");
        }
        this.deadline = deadline;
    }

    public void setEstimatedHours(BigDecimal estimatedHours) {
        if (estimatedHours == null) {
            throw new IllegalArgumentException("Estimerede timer kan ikke være null.");
        }
        if (estimatedHours.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estimerede timer kan ikke være negative.");
        }
        this.estimatedHours = estimatedHours;
    }

    public void setUsedHours(BigDecimal usedHours) {
        if (usedHours == null) {
            throw new IllegalArgumentException("Brugte timer kan ikke være null.");
        }
        if (usedHours.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Brugte timer kan ikke være negative.");
        }
        this.usedHours = usedHours;
    }

    public void setCompletionPercentage(int completionPercentage) {
        if (completionPercentage < 0 || completionPercentage > 100) {
            throw new IllegalArgumentException("Færdiggørelsesprocent for task skal være mellem 0 og 100.");
        }
        this.completionPercentage = completionPercentage;
    }

    public void setStatus(StateStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Task status kan ikke være null.");
        }
        this.status = status;
    }

    public void setSubprojectId(int subprojectId) {
        if (subprojectId <= 0) {
            throw new IllegalArgumentException("Subproject ID for task skal være et positivt tal.");
        }
        this.subprojectId = subprojectId;
    }

    /** Override toString() method for better readability **/
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deadline=" + deadline +
                ", estimatedHours=" + estimatedHours +
                ", usedHours=" + usedHours +
                ", completionPercentage=" + completionPercentage +
                ", status=" + status +
                ", subprojectId=" + subprojectId +
                '}';
    }
}