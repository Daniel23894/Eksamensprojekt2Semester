package repository;

import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskAssignmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Tildeler et teammedlem til en opgave
    public void assignMember(int taskId, int memberId) {
        String sql = "INSERT INTO task_assignments (task_id, member_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    // Fjerner tildelingen af et teammedlem fra en opgave
    public void unassignMember(int taskId, int memberId) {
        String sql = "DELETE FROM task_assignments WHERE task_id = ? AND member_id = ?";
        jdbcTemplate.update(sql, taskId, memberId);
    }

    // Henter alle medlemmer, der er tildelt en opgave
    public List<TeamMember> getAssignedMembers(int taskId) {
        String sql = "SELECT tm.* FROM team_member tm " +
                "JOIN task_assignments ta ON tm.member_Id = ta.member_id " +
                "WHERE ta.task_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TeamMember.class), taskId);
    }

    /** Fjerner alle tildelinger for en bestemt opgave **/
    public void removeAllAssignmentsForTask(int taskId) {
        String sql = "DELETE FROM task_assignments WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }

}
