package repository;

import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TaskAssignmentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void assignMember(int taskId, int memberId) {
        String sql = "INSERT INTO task_assignments (task_id, member_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    public void unassignMember(int taskId, int memberId) {
        String sql = "DELETE FROM task_assignments WHERE task_id = ? AND member_id = ?";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    public List<TeamMember> getAssignedMembers(int taskId) {
        String sql = "SELECT tm.* FROM team_member tm " +
                "JOIN task_assignments ta ON tm.member_Id = ta.member_id " +
                "WHERE ta.task_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TeamMember.class), taskId);
    }
}