package dev.mvc.log;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class LogDAO implements LogDAOInter {

    private final JdbcTemplate jdbcTemplate;

    public LogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LogVO> getLogs(String userType) {
        String sql = "SELECT * FROM log";
        if ("admin".equals(userType)) {
            sql += " WHERE memberno = (SELECT memberno FROM member WHERE grade = 1)";
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> new LogVO(
                rs.getInt("logno"),
                rs.getString("ip"),
                rs.getDate("logdate"),
                rs.getInt("memberno")
        ));
    }
}
