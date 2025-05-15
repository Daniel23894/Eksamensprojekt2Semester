package com.example.eksamensprojekt2semester.repository;

import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Subproject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubprojectRepository {

    private final JdbcTemplate jdbcTemplate;

    /** Constructor-based dependency injection **/
    public SubprojectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** RowMapper to convert SQL result to Subproject object **/
    private final RowMapper<Subproject> subprojectRowMapper = (rs, rowNum) -> {
        Subproject subproject = new Subproject();
        subproject.setId(rs.getInt("subProjectId"));
        subproject.setName(rs.getString("subProjectName"));
        subproject.setCompletionPercentage(rs.getInt("completionPercentage"));
        subproject.setStatus(StateStatus.fromValue(rs.getInt("statusId")));
        subproject.setProjectId(rs.getInt("projectId"));
        return subproject;
    };

    /** CREATE **/
    public void save(Subproject subproject) {
        String sql = "INSERT INTO subProject (subProjectName, completionPercentage, statusId, projectId) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId());
    }

    /** READ (find by ID) **/
    public Optional<Subproject> findById(int id) {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId FROM subProject WHERE subProjectId = ?";
        try {
            Subproject subproject = jdbcTemplate.queryForObject(sql, subprojectRowMapper, id);
            return Optional.ofNullable(subproject);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /** READ (find all subprojects by projectId) **/
    public List<Subproject> findAllByProjectId(int projectId) {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId FROM subProject WHERE projectId = ?";
        return jdbcTemplate.query(sql, subprojectRowMapper, projectId);
    }

    /** READ (all subprojects) **/
    public List<Subproject> findAll() {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId FROM subProject";
        return jdbcTemplate.query(sql, subprojectRowMapper);
    }

    /** UPDATE **/
    public void update(Subproject subproject) {
        String sql = "UPDATE subProject SET subProjectName = ?, completionPercentage = ?, statusId = ?, projectId = ? WHERE subProjectId = ?";
        int affectedRows = jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId(),
                subproject.getId()
        );
        if (affectedRows == 0) {
            throw new RuntimeException("Kunne ikke opdatere subprojekt med ID: " + subproject.getId() + ". Subprojektet blev ikke fundet i databasen.");
        }
    }

    /** DELETE **/
    public void deleteById(int id) {
        String sql = "DELETE FROM subProject WHERE subProjectId = ?";
        jdbcTemplate.update(sql, id);
    }

    /** UTILITY: Check if subproject exists by ID **/
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM subProject WHERE subProjectId = ? LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}