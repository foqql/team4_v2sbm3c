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
        String sql = "SELECT l.logno, l.ip, l.logdate, l.memberno, m.mname " +
                     "FROM log l " +
                     "JOIN member m ON l.memberno = m.memberno";
        if ("admin".equals(userType)) {
            sql += " WHERE m.grade = 1";
        }
        sql += " ORDER BY l.logno"; // 로그 번호 순서로 정렬

        return jdbcTemplate.query(sql, (rs, rowNum) -> new LogVO(
                rs.getInt("logno"),
                rs.getString("ip"),
                rs.getTimestamp("logdate"), // Timestamp로 변경하여 시간을 제대로 가져옴
                rs.getInt("memberno"),
                rs.getString("mname")
        ));
    }



    @Override
    public void deleteLog(int logno) {
        // 로그 삭제
        String sqlDelete = "DELETE FROM log WHERE logno = ?";
        jdbcTemplate.update(sqlDelete, logno);
    }
}
