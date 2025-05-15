package com.example.eksamensprojekt2semester.repository;

import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskAssignmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Assign a team member to a task
    public void assignMember(int taskId, int memberId) {
        String sql = "INSERT INTO taskAssignment (taskId, memberId) VALUES (?, ?)";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    // Remove assignment of a team member from a task
    public void unassignMember(int taskId, int memberId) {
        String sql = "DELETE FROM taskAssignment WHERE taskId = ? AND memberId = ?";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    // Get all members assigned to a task
    public List<TeamMember> getAssignedMembers(int taskId) {
        String sql = "SELECT tm.* FROM teamMember tm " +
                "JOIN taskAssignment ta ON tm.memberId = ta.memberId " +
                "WHERE ta.taskId = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TeamMember.class), taskId);
    }

    // Remove all assignments for a specific task
    public void removeAllAssignmentsForTask(int taskId) {
        String sql = "DELETE FROM taskAssignment WHERE taskId = ?";
        jdbcTemplate.update(sql, taskId);
    }
}