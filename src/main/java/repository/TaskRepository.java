package repository;

import com.zaxxer.hikari.HikariDataSource;
import model.StateStatus;
import model.Task;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    /** Constructor-based dependency injection **/
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Task> taskRowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getInt("taskId"));
        task.setName(rs.getString("taskName"));
        task.setDeadline(rs.getObject("deadline", LocalDate.class)); /** Get the value from the deadline column and convert it to a LocalDate **/
        task.setEstimatedHours(rs.getBigDecimal("estimatedHours"));
        task.setUsedHours(rs.getBigDecimal("usedHours"));
        task.setCompletionPercentage(rs.getInt("completionPercentage"));
        task.setStatus(StateStatus.fromValue(rs.getInt("statusId"))); /** Gets an int that represents an enum value, converts the integer from the database into a matching StateStatus enum constant **/
        task.setSubprojectId(rs.getInt("subprojectId"));
        return task;
    };

    /** CREATE **/
    public void save(Task task) {
        String sql = "INSERT INTO Task (taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                task.getName(),
                task.getDeadline(),
                task.getEstimatedHours(),
                task.getUsedHours(),
                task.getCompletionPercentage(),
                task.getStatus().getValue(),
                task.getSubprojectId());
    }

    /** READ **/
    public Task getTaskById(int id) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId " +
                "FROM Task WHERE taskId = ?";
        try {
            /** QueryForObject expects exactly one row. Uses RowMapper to convert it **/
            return jdbcTemplate.queryForObject(sql, taskRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            /** If no row is found, we return null **/
            return null;
        }
    }

    public Optional<Task> findById(int id) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId " +
                "FROM Task WHERE taskId = ?";
        try {
            /** QueryForObject expects exactly one row. Uses RowMapper to convert it **/
            Task task = jdbcTemplate.queryForObject(sql, taskRowMapper, id);
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            /** If no row is found return an empty Optional to safely represent 'no result', instead of returning null which can cause NullPointerException **/
            return Optional.empty();
        }
    }

    public List<Task> findAll() {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId " +
                "FROM Task";
        /** Returns a list of objects. Uses RowMapper to convert each row **/
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public List<Task> findBySubprojectId(int subprojectId) {
        String sql = "SELECT taskId, taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId " +
                "FROM Task WHERE subprojectId = ?";
        /** Returns a list of objects. Uses RowMapper to convert each row **/
        return jdbcTemplate.query(sql, taskRowMapper, subprojectId);
    }

    /** UPDATE **/
    public void update(Task task) {
        String sql = "UPDATE Task SET taskName = ?, deadline = ?, estimatedHours = ?, usedHours = ?, " +
                "completionPercentage = ?, statusId = ?, subprojectId = ? WHERE taskId = ?";

        /** jdbcTemplate.update() returns an integer (int) that tells how many rows in the database were actually changed by your UPDATE command **/
        int affectedRows = jdbcTemplate.update(sql,
                task.getName(),
                task.getDeadline(),
                task.getEstimatedHours(),
                task.getUsedHours(),
                task.getCompletionPercentage(),
                task.getStatus().getValue(),
                task.getSubprojectId(),
                task.getId()
        );
        /** If affectedRows is 0, it means no rows were updated **/
        if (affectedRows == 0) {
            throw new RuntimeException("Kunne ikke opdatere task med ID: " + task.getId() + ". Tasken blev ikke fundet i databasen.");
        }
    }

    /** DELETE **/
    public void deleteById(int id) {
        String sql = "DELETE FROM Task WHERE taskId = ?";
        jdbcTemplate.update(sql, id);
    }

    /** UTILITY **/
    public static boolean existsById(int id) {
        /** 1. SQL Query returns 1 for each row that matches the where condition **/
        String sql = "SELECT 1 FROM Task WHERE taskId = ? LIMIT 1";

        try {
            /** 2. Expected Java type for the output from the SELECT part (the result 1 should be treated as an Integer) **/
            jdbcTemplate.queryForObject(sql, Integer.class, id);

            /** 3. If task exists (no errors) **/
            return true;

        } catch (EmptyResultDataAccessException e) {
            /** 4. The task does not exist **/
            return false;
        }
    }
}