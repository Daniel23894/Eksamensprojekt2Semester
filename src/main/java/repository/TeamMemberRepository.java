package repository;

import com.zaxxer.hikari.HikariDataSource;
import model.TeamMember;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class TeamMemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public TeamMemberRepository(HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<TeamMember> teamMemberRowMapper = (rs, rowNum) -> {
        TeamMember teamMember = new TeamMember();
        teamMember.setMemberId(rs.getInt("member_id"));
        teamMember.setName(rs.getString("name"));
        teamMember.setEmail(rs.getString("email"));
        teamMember.setRole(rs.getString("role"));
        return teamMember;
    };

    public List<TeamMember> findAll() {
        String sql = "SELECT * FROM team_member";
        return jdbcTemplate.query(sql, teamMemberRowMapper);
    }

    public boolean existsById(int memberId) {
        String sql = "SELECT COUNT(*) FROM team_member WHERE member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count != null && count > 0;
    }
}