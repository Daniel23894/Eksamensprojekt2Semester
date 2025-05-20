package com.example.eksamensprojekt2semester.repository;

import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Task;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Task> taskRowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getInt("taskId"));
        task.setName(rs.getString("taskName"));
        task.setDeadline(rs.getObject("deadline", LocalDate.class));
        task.setEstimatedHours(rs.getBigDecimal("estimatedHours"));
        task.setUsedHours(rs.getBigDecimal("usedHours"));
        task.setCompletionPercentage(rs.getInt("completionPercentage"));
        task.setStatus(StateStatus.fromValue(rs.getInt("statusId")));
        task.setSubprojectId(rs.getInt("subProjectId"));
        return task;
    };

    /** CREATE **/
    public int save(Task task) {
        String sql = "INSERT INTO task (taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subProjectId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getName());
            ps.setObject(2, task.getDeadline());
            ps.setBigDecimal(3, task.getEstimatedHours());
            ps.setBigDecimal(4, task.getUsedHours());
            ps.setInt(5, task.getCompletionPercentage());
            ps.setInt(6, task.getStatus() != null ? task.getStatus().getValue() : StateStatus.NOT_STARTED.getValue());
            ps.setInt(7, task.getSubprojectId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /** READ **/
    public Task getTaskById(int id) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subProjectId " +
                "FROM task WHERE taskId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, taskRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Optional<Task> findById(int id) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId " +
                "FROM task WHERE taskId = ?";
        try {
            Task task = jdbcTemplate.queryForObject(sql, taskRowMapper, id);
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Task> findAll() {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subProjectId " +
                "FROM task";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public List<Task> findBySubprojectId(int subprojectId) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subProjectId " +
                "FROM task WHERE subprojectId = ?";
        return jdbcTemplate.query(sql, taskRowMapper, subprojectId);
    }

    public List<Task> findByProjectId(int projectId) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subProjectId " +
                "FROM task WHERE projectId = ?";
        return jdbcTemplate.query(sql, taskRowMapper, projectId);
    }

    /** Counts tasks based on project ID and status **/
    public int countByProjectIdAndStatus(int projectId, StateStatus status) {
        String sql = "SELECT COUNT(*) FROM task WHERE projectId = ? AND statusId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId, status.getValue());
    }

    /** Counts tasks based on project ID **/
    public int countByProjectId(int projectId) {
        String sql = "SELECT COUNT(*) FROM task WHERE projectId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId);
    }

    /** Counts tasks based on subproject ID and status **/
    public int countBySubprojectIdAndStatus(int subprojectId, StateStatus status) {
        String sql = "SELECT COUNT(*) FROM task WHERE subprojectId = ? AND statusId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, subprojectId, status.getValue());
    }

    /** Counts tasks based on subproject ID **/
    public int countBySubprojectId(int subprojectId) {
        String sql = "SELECT COUNT(*) FROM task WHERE subprojectId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, subprojectId);
    }

    /** UPDATE **/
    public void update(Task task) {
        String sql = "UPDATE task SET taskName = ?, deadline = ?, estimatedHours = ?, usedHours = ?, " +
                "completionPercentage = ?, statusId = ?, subprojectId = ? WHERE taskId = ?";

        int affectedRows = jdbcTemplate.update(sql,
                task.getName(),
                task.getDeadline(),
                task.getEstimatedHours(),
                task.getUsedHours(),
                task.getCompletionPercentage(),
                task.getStatus() != null ? task.getStatus().getValue() : StateStatus.NOT_STARTED.getValue(),
                task.getSubprojectId(),
                task.getId());

        if (affectedRows == 0) {
            throw new RuntimeException("Could not update task with ID: " + task.getId() + ". Task not found.");
        }
    }

    /** DELETE **/
    public void deleteById(int id) {
        String sql = "DELETE FROM task WHERE taskId = ?";
        jdbcTemplate.update(sql, id);
    }

    /** UTILITY **/
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM task WHERE taskId = ? LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}