package repository;

import com.zaxxer.hikari.HikariDataSource;
import model.StateStatus;
import model.Subproject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubprojectRepository {
    private final JdbcTemplate jdbcTemplate;;

    public SubprojectRepository(JdbcTemplate jdbcTemplate, HikariDataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<Subproject> subprojectRowMapper = (rs, rowNum) -> {
        Subproject subproject = new Subproject();
        subproject.setId(rs.getInt("subprojectId"));
        subproject.setName(rs.getString("subprojectName"));
        subproject.setCompletionPercentage(rs.getInt("completionPercentage"));
        subproject.setStatus(StateStatus.fromValue(rs.getInt("statusId")));
        subproject.setProjectId(rs.getInt("ProjectId"));
        return subproject;
    };

    /** CREATE **/
    public void save(Subproject subproject){
        String sql = "INSERT INTO Subproject (subprojectName, completionPercentage, statusId, ProjectId) VALUES (?, ?. ?. ?)";
        jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId());
    }

    /**  READ **/
    public Optional<Subproject> findById(int id) {
        String sql = "SELECT subprojectId, subprojectName, completionPercentage, statusId, ProjectId FROM Subproject WHERE subprojectId = ?";
        try {
            /** QueryForObject expects exactly one row. Uses RowMapper to convert it **/
            Subproject subproject = jdbcTemplate.queryForObject(sql, subprojectRowMapper, id);
            return Optional.ofNullable(subproject);
        } catch (EmptyResultDataAccessException e) {
            /** If no row is found, we return an empty Optional **/
            return Optional.empty();
        }
    }

    public List<Subproject> findAllByProjectId(int projectId) {
        String sql = "SELECT subprojectId, subprojectName, completionPercentage, statusId, ProjectId FROM Subproject WHERE ProjectId = ?";
        /** Returns a list of objects. Uses RowMapper to convert each row **/
        return jdbcTemplate.query(sql, subprojectRowMapper, projectId);
    }

    public List<Subproject> findAll() {
        String sql = "SELECT subprojectId, subprojectName, completionPercentage, statusId, ProjectId FROM Subproject";
        return jdbcTemplate.query(sql, subprojectRowMapper);
    }

    /**  UPDATE **/
    public void update(Subproject subproject) {
        String sql = "UPDATE Subproject SET subprojectName = ?, completionPercentage = ?, statusId = ?, ProjectId = ? WHERE subprojectId = ?";

        /** jdbcTemplate.update() returns an integer (int) that tells how many rows in the database were actually changed by your UPDATE command **/
        int affectedRows = jdbcTemplate.update(sql,
                subproject.getName(),
                subproject.getCompletionPercentage(),
                subproject.getStatus().getValue(),
                subproject.getProjectId(),
                subproject.getId()
        );
        /** If affectedRows is 0, it means no rows were updated **/
        if (affectedRows == 0) {
            throw new RuntimeException("Kunne ikke opdatere subprojekt med ID: " + subproject.getId() + ". Subprojektet blev ikke fundet i databasen.");
        }
    }

    /** DELETE **/
    public void deleteById(int id) {
        String sql = "DELETE FROM Subproject WHERE subprojectId = ?";
        jdbcTemplate.update(sql, id);
    }

    /** UTILITY **/
    public boolean existsById(int id) {
        /** 1. SQL Query returns 1 for each row that matches the where condition **/
        String sql = "SELECT 1 FROM Subproject WHERE subprojectId = ? LIMIT 1";

        try {
            /** 2.Expected Java type for the output from the SELECT part (the result 1 should be treated as an Integer)**/
            jdbcTemplate.queryForObject(sql, Integer.class, id);

            /** 3. If subproject exists (no errors) **/
            return true;

        } catch (EmptyResultDataAccessException e) {
            /** 4.  The subproject does not exist **/
            return false;
        }
    }

}

