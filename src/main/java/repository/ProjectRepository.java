package repository;

import com.zaxxer.hikari.HikariDataSource;
import model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Project> projectRowMapper = (rs, rowNum) -> {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getDate("start_date").toLocalDate());
        project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        project.setStatus(rs.getString("status"));
        project.setBudget(rs.getBigDecimal("budget"));
        return project;
    };

    public Project save(Project project) {
        String sql = "INSERT INTO project (name, description, start_date, end_date, status, budget) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, project.getName(), project.getDescription(), project.getStartDate(),
                project.getEndDate(), project.getStatus(), project.getBudget());
        return project;
    }

    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        return jdbcTemplate.query(sql, projectRowMapper);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM project WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) != null;
    }
}
