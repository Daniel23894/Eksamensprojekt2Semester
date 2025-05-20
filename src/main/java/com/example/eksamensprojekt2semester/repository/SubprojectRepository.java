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

    public SubprojectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Subproject> subprojectRowMapper = (rs, rowNum) -> {
        Subproject subproject = new Subproject();
        subproject.setId(rs.getInt("subProjectId")); // Match database column name
        subproject.setName(rs.getString("subProjectName"));
        subproject.setCompletionPercentage(rs.getInt("completionPercentage"));
        subproject.setStatus(StateStatus.fromValue(rs.getInt("statusId")));
        subproject.setProjectId(rs.getInt("projectId"));
        return subproject;
    };

    /** CREATE **/
    public void save(Subproject subproject) {
        String sql = "INSERT INTO subProject (subProjectName, completionPercentage, statusId, projectId) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId());
    }

    /** READ **/
    public Subproject getSubprojectById(int id) {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId " +
                "FROM subProject WHERE subProjectId = ?"; // Use correct table and column name
        try {
            return jdbcTemplate.queryForObject(sql, subprojectRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Optional<Subproject> findById(int id) {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId " +
                "FROM subProject WHERE subProjectId = ?"; // Use correct table and column name
        try {
            Subproject subproject = jdbcTemplate.queryForObject(sql, subprojectRowMapper, id);
            return Optional.ofNullable(subproject);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Subproject> findAll() {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId " +
                "FROM subProject"; // Use correct table name
        return jdbcTemplate.query(sql, subprojectRowMapper);
    }

    public List<Subproject> findByProjectId(int projectId) {
        String sql = "SELECT subProjectId, subProjectName, completionPercentage, statusId, projectId " +
                "FROM subProject WHERE projectId = ?"; // Use correct table name
        return jdbcTemplate.query(sql, subprojectRowMapper, projectId);
    }

    /** UPDATE **/
    public void update(Subproject subproject) {
        String sql = "UPDATE subProject SET subProjectName = ?, completionPercentage = ?, statusId = ?, projectId = ? " +
                "WHERE subProjectId = ?"; // Use correct table and column name

        int affectedRows = jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId(),
                subproject.getId());

        if (affectedRows == 0) {
            throw new RuntimeException("Could not update subproject with ID: " + subproject.getId() + ". Subproject not found.");
        }
    }

    /** DELETE **/
    public void deleteById(int id) {
        String sql = "DELETE FROM subProject WHERE subProjectId = ?"; // Use correct table and column name
        jdbcTemplate.update(sql, id);
    }

    /** UTILITY - This is the method causing your error **/
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM subProject WHERE subProjectId = ? LIMIT 1"; // Fixed: use correct table and column name
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    /** Count subprojects by project ID **/
    public int countByProjectId(int projectId) {
        String sql = "SELECT COUNT(*) FROM subProject WHERE projectId = ?"; // Use correct table name
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId);
    }

    /** Count subprojects by project ID and status **/
    public int countByProjectIdAndStatus(int projectId, StateStatus status) {
        String sql = "SELECT COUNT(*) FROM subProject WHERE projectId = ? AND statusId = ?"; // Use correct table name
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId, status.getValue());
    }
}