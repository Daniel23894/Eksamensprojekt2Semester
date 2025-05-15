package com.example.eksamensprojekt2semester.repository;

import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamMemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TeamMember> teamMemberRowMapper = (rs, rowNum) -> {
        TeamMember teamMember = new TeamMember();
        teamMember.setMemberId(rs.getInt("member_id"));
        teamMember.setName(rs.getString("name"));
        teamMember.setEmail(rs.getString("email"));
        teamMember.setRole(rs.getString("role"));
        teamMember.setHoursPerDay(rs.getBigDecimal("hours_per_day"));
        return teamMember;
    };

    /** Henter alle teammedlemmer fra databasen **/
    public List<TeamMember> findAll() {
        String sql = "SELECT * FROM team_member";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TeamMember.class));
    }

    /** Henter et teammedlem baseret på ID **/
    public TeamMember findById(int memberId) {
        String sql = "SELECT * FROM team_member WHERE member_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TeamMember.class), memberId);
        } catch (Exception e) {
            return null;
        }
    }

    /** Henter alle teammedlemmer tilknyttet et projekt baseret på projectId **/
    public List<TeamMember> findByProjectId(int projectId) {
        String sql = "SELECT * FROM team_member WHERE project_id = ?";
        return jdbcTemplate.query(sql, teamMemberRowMapper, projectId);
    }

    /** Tæller antal teammedlemmer i et projekt **/
    public int countByProjectId(int projectId) {
        String sql = "SELECT COUNT(*) FROM team_member WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId);
    }

    /** Tjekker om et teammedlem eksisterer baseret på memberId **/
    public boolean existsById(int memberId) {
        String sql = "SELECT COUNT(1) FROM team_member WHERE member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count != null && count > 0;
    }

    /** Gemmer et teammedlem (opdaterer hvis eksisterer, ellers opretter) **/
    public TeamMember save(TeamMember teamMember) {
        if (existsById(teamMember.getMemberId())) {
            String sql = "UPDATE team_member SET name = ?, email = ?, role = ?, hours_per_day = ? WHERE member_id = ?";
            jdbcTemplate.update(sql,
                    teamMember.getName(),
                    teamMember.getEmail(),
                    teamMember.getRole(),
                    teamMember.getHoursPerDay(),
                    teamMember.getMemberId());
        } else {
            String sql = "INSERT INTO team_member (name, email, role, hours_per_day) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    teamMember.getName(),
                    teamMember.getEmail(),
                    teamMember.getRole(),
                    teamMember.getHoursPerDay());
        }
        return teamMember;
    }
}
