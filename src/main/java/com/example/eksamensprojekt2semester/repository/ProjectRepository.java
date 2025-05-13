package com.example.eksamensprojekt2semester.repository;

import com.zaxxer.hikari.HikariDataSource;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.model.StateStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** Reusable RowMapper **/
    private RowMapper<Project> projectRowMapper = (rs, rowNum) -> {
        Project project = new Project();
        project.setProjectId(rs.getInt("id"));
        project.setName(rs.getString("name"));
        project.setStartDate(rs.getDate("start_date").toLocalDate());
        project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        project.setActualStartDate(rs.getDate("actual_start_date") != null ? rs.getDate("actual_start_date").toLocalDate() : null);
        project.setActualEndDate(rs.getDate("actual_end_date") != null ? rs.getDate("actual_end_date").toLocalDate() : null);
        project.setBudget(rs.getBigDecimal("budget"));
        project.setCompletionPercentage(rs.getInt("completion_percentage"));
        project.setStatus(StateStatus.fromValue(rs.getInt("status_id"))); /** Handels id from the database as int, and converts it into a matching enum value**/
        return project;
    };

    // Metode til at gemme et nyt projekt i databasen
    public Project save(Project project) {
        String sql = "INSERT INTO project (name, description, start_date, end_date, actual_start_date, actual_end_date, budget, completion_percentage, status_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Udfører opdateringen
        jdbcTemplate.update(sql, project.getName(), project.getStartDate(),
                project.getEndDate(), project.getActualStartDate(), project.getActualEndDate(), project.getBudget(),
                project.getCompletionPercentage(), project.getStatus());
        return project; // Returnerer det gemte projekt
    }

    // Metode til at hente alle projekter fra databasen
    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        // Bruger ResultSetExtractor for at konvertere resultater til en liste af projekter
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Project project = new Project();
            project.setProjectId(rs.getInt("id"));
            project.setName(rs.getString("name"));
            project.setStartDate(rs.getDate("start_date").toLocalDate());
            project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
            project.setActualStartDate(rs.getDate("actual_start_date") != null ? rs.getDate("actual_start_date").toLocalDate() : null);
            project.setActualEndDate(rs.getDate("actual_end_date") != null ? rs.getDate("actual_end_date").toLocalDate() : null);
            project.setBudget(rs.getBigDecimal("budget"));
            project.setCompletionPercentage(rs.getInt("completion_percentage"));
            project.setStatus(StateStatus.fromValue(rs.getInt("status_id"))); /** Konverterer status ID til enum **/
            return project;
        });
    }

    // Metode til at kontrollere, om et projekt eksisterer baseret på projektets ID
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM project WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) != null;
    }

    // Metode til at hente et projekt baseret på ID
    public Project findById(int id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        List<Project> projects = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Project project = new Project();
            project.setProjectId(rs.getInt("id"));
            project.setName(rs.getString("name"));
            project.setStartDate(rs.getDate("start_date").toLocalDate());
            project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
            project.setActualStartDate(rs.getDate("actual_start_date") != null ? rs.getDate("actual_start_date").toLocalDate() : null);
            project.setActualEndDate(rs.getDate("actual_end_date") != null ? rs.getDate("actual_end_date").toLocalDate() : null);
            project.setBudget(rs.getBigDecimal("budget"));
            project.setCompletionPercentage(rs.getInt("completion_percentage"));
            project.setStatus(StateStatus.fromValue(rs.getInt("status_id"))); /** Konverterer status ID til enum **/
            return project;
        }, id);
        return (projects.isEmpty()) ? null : projects.get(0);
    }

    /** Find projects by name containing a specific string **/
    public List<Project> findByNameContaining(String name) {
        String sql = "SELECT * FROM project WHERE name LIKE ?";

        /** "%": mean there can be text before and after 'name',
         *  projects that contain the searched keyword anywhere in the title will show up **/
        return jdbcTemplate.query(sql, projectRowMapper, "%" + name + "%");
    }

    /** Find projects by status **/
    public List<Project> findByStatus(StateStatus status) {
        String sql = "SELECT * FROM project WHERE status_id = ?";
        return jdbcTemplate.query(sql, projectRowMapper, status.getValue());  // Get projects with a specific status
    }

    /** Find projects by name and status **/
    public List<Project> findByNameContainingAndStatus(String name, StateStatus status) {
        String sql = "SELECT * FROM project WHERE name LIKE ? AND status_id = ?";
        return jdbcTemplate.query(sql, projectRowMapper, "%" + name + "%", status.getValue());  // Projects that match both criteria
    }

    // Metode til at opdatere et eksisterende projekt i databasen
    public void update(Project project) {
        String sql = "UPDATE project SET name = ?, description = ?, start_date = ?, end_date = ?, actual_start_date = ?, actual_end_date = ?, budget = ?, completion_percentage = ?, status_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, project.getName(), project.getStartDate(),
                project.getEndDate(), project.getActualStartDate(), project.getActualEndDate(), project.getBudget(),
                project.getCompletionPercentage(), project.getStatus(), project.getProjectId());
    }

    // Metode til at slette et projekt baseret på ID
    public void delete(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
