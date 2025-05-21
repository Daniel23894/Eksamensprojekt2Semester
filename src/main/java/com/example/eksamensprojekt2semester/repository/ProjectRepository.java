package com.example.eksamensprojekt2semester.repository;

import com.zaxxer.hikari.HikariDataSource;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.model.StateStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Project> projectRowMapper = (rs, rowNum) -> {
        Project project = new Project();
        project.setProjectId(rs.getInt("projectId"));
        project.setName(rs.getString("projectName"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getDate("startDate").toLocalDate());
        project.setEndDate(rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null);
        project.setActualStartDate(rs.getDate("actualStartDate") != null ? rs.getDate("actualStartDate").toLocalDate() : null);
        project.setActualEndDate(rs.getDate("actualEndDate") != null ? rs.getDate("actualEndDate").toLocalDate() : null);
        project.setBudget(rs.getBigDecimal("budget"));
        project.setCompletionPercentage(rs.getInt("completionPercentage"));
        project.setStatus(StateStatus.fromValue(rs.getInt("statusId")));
        return project;
    };

    public Project save(Project project) {
        int statusId = project.getStatus().getValue();

        String sql = "INSERT INTO project (projectName, description, startDate, endDate, actualStartDate, actualEndDate, budget, completionPercentage, statusId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        jdbcTemplate.update(sql, project.getName(), project.getDescription(), project.getStartDate(),
                project.getEndDate(), project.getActualStartDate(), project.getActualEndDate(), project.getBudget(),
                project.getCompletionPercentage(), statusId);

        String sqlGetLastInsertId = "SELECT LAST_INSERT_ID()";
        int projectId = jdbcTemplate.queryForObject(sqlGetLastInsertId, Integer.class);
        project.setProjectId(projectId);

        return project;
    }

    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        return jdbcTemplate.query(sql, projectRowMapper);
    }

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM project WHERE projectId = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) != null;
    }

    public Project findById(int id) {
        String sql = "SELECT * FROM project WHERE projectId = ?";
        List<Project> projects = jdbcTemplate.query(sql, projectRowMapper, id);
        return (projects.isEmpty()) ? null : projects.get(0);
    }

    public List<Project> findByNameContaining(String name) {
        String sql = "SELECT * FROM project WHERE projectName LIKE ?";
        return jdbcTemplate.query(sql, projectRowMapper, "%" + name + "%");
    }

    public List<Project> findByStatus(StateStatus status) {
        String sql = "SELECT * FROM project WHERE statusId = ?";
        return jdbcTemplate.query(sql, projectRowMapper, status.getValue());
    }

    public List<Project> findByNameContainingAndStatus(String name, StateStatus status) {
        String sql = "SELECT * FROM project WHERE projectName LIKE ? AND statusId = ?";
        return jdbcTemplate.query(sql, projectRowMapper, "%" + name + "%", status.getValue());
    }

    /** Returns number of affected rows, to inform service that the update failed because the project didn’t exist**/
    public int update(Project project) {
        String sql = "UPDATE project SET projectName = ?, description = ?, startDate = ?, endDate = ?, actualStartDate = ?, actualEndDate = ?, budget = ?, completionPercentage = ?, statusId = ? " +
                "WHERE projectId = ?";
        return jdbcTemplate.update(sql,
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getActualStartDate(),
                project.getActualEndDate(),
                project.getBudget(),
                project.getCompletionPercentage(),
                project.getStatus().getValue(),
                project.getProjectId());
    }

    /** Returns number of affected rows, to inform service that delete failed because the project didn’t exist**/
    public int delete(int id) {
        String sql = "DELETE FROM project WHERE projectId = ?";
        return jdbcTemplate.update(sql, id);
    }
}
