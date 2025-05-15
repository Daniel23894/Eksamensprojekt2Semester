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

    private final RowMapper<TeamMember> teamMemberRowMapper = (rs, rowNum) -> {
        TeamMember teamMember = new TeamMember();
        teamMember.setMemberId(rs.getInt("memberId"));
        teamMember.setName(rs.getString("name"));
        teamMember.setEmail(rs.getString("email"));
        teamMember.setPassword(rs.getString("password"));
        teamMember.setRole(rs.getString("role"));
        teamMember.setHoursPerDay(rs.getBigDecimal("hoursPerDay"));
        return teamMember;
    };

    /** Henter alle teammedlemmer tilknyttet et projekt baseret på projectId **/
    public List<TeamMember> findByProjectId(int projectId) {
        String sql = "SELECT * FROM teamMember WHERE project_id = ?";
        return jdbcTemplate.query(sql, teamMemberRowMapper, projectId);
    }

    /** Tæller antal teammedlemmer i et projekt **/
    public int countByProjectId(int projectId) {
        String sql = "SELECT COUNT(*) FROM teamMember WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, projectId);
    }

    /** Fetch all team members **/
    public List<TeamMember> findAll() {
        String sql = "SELECT * FROM teamMember";
        return jdbcTemplate.query(sql, teamMemberRowMapper);
    }

    /** Fetch team member by memberId **/
    public TeamMember findById(int memberId) {
        String sql = "SELECT * FROM teamMember WHERE memberId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, teamMemberRowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    public TeamMember findByEmail(String email) {
        String sql = "SELECT * FROM teamMember WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, teamMemberRowMapper, email);
        } catch (Exception e) {
            /** No user found or other query issue **/
            return null;
        }
    }


    /** Check if team member exists by memberId **/
    public boolean existsById(int memberId) {
        String sql = "SELECT COUNT(1) FROM teamMember WHERE memberId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count != null && count > 0;
    }

    /** Save or update a team member **/
    public TeamMember save(TeamMember teamMember) {
        if (existsById(teamMember.getMemberId())) {
            String sql = "UPDATE teamMember SET name = ?, email = ?, password = ?, role = ?, hoursPerDay = ? WHERE memberId = ?";
            jdbcTemplate.update(sql,
                    teamMember.getName(),
                    teamMember.getEmail(),
                    teamMember.getPassword(),
                    teamMember.getRole(),
                    teamMember.getHoursPerDay(),
                    teamMember.getMemberId());
        } else {
            String sql = "INSERT INTO teamMember (name, email, password, role, hoursPerDay) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    teamMember.getName(),
                    teamMember.getEmail(),
                    teamMember.getPassword(),
                    teamMember.getRole(),
                    teamMember.getHoursPerDay());
        }
        return teamMember;
    }

    /** Delete a team member by memberId **/
    public void deleteById(int memberId) {
        String sql = "DELETE FROM teamMember WHERE memberId = ?";
        jdbcTemplate.update(sql, memberId);
    }


}
