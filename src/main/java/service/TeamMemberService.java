package service;

import model.TeamMember;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TeamMemberService {
    private JdbcTemplate jdbcTemplate;

    public static boolean existsById(int memberId) {
        // Simulate a check in the database
        return memberId >= 0 && memberId < 100; // Example condition
    }

    public List<TeamMember> getAllMembers() {
        String sql = "SELECT * FROM team_member";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TeamMember.class));
    }
}
