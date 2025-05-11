package dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import model.StateStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectDTO {
    private int id;

    /** Using Bean Validation annotations, to ensure that object fields meet specific constraints **/
    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

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



}