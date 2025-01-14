package dev.mvc.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class NoticeDAO implements NoticeDAOInter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int create(NoticeVO noticeVO) {
        int notino = jdbcTemplate.queryForObject("SELECT notice_seq.NEXTVAL FROM dual", Integer.class);
        String sql = "INSERT INTO notice (notino, title, content, nodate) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        return jdbcTemplate.update(sql, notino, noticeVO.getTitle(), noticeVO.getContent());
    }

    @Override
    public NoticeVO read(int notino) {
        String sql = "SELECT * FROM notice WHERE notino = ?";
        return jdbcTemplate.queryForObject(sql, new NoticeRowMapper(), notino);
    }

    @Override
    public int update(NoticeVO noticeVO) {
        String sql = "UPDATE notice SET title = ?, content = ? WHERE notino = ?";
        return jdbcTemplate.update(sql, noticeVO.getTitle(), noticeVO.getContent(), noticeVO.getNotino());
    }

    @Override
    public int delete(int notino) {
        String sql = "DELETE FROM notice WHERE notino = ?";
        return jdbcTemplate.update(sql, notino); // 데이터베이스에서 해당 레코드 삭제
    }

    @Override
    public List<NoticeVO> list() {
        String sql = "SELECT * FROM notice ORDER BY notino ASC";
        return jdbcTemplate.query(sql, new NoticeRowMapper());
    }

    public void resetSequence() {
        String sql = "DROP SEQUENCE notice_seq";
        jdbcTemplate.execute(sql);

        sql = "CREATE SEQUENCE notice_seq START WITH 1 INCREMENT BY 1";
        jdbcTemplate.execute(sql);
    }

    public void renumberNotices() {
        String sql = "DECLARE "
                   + "CURSOR c IS SELECT rowid, ROWNUM AS new_no FROM notice ORDER BY notino; "
                   + "BEGIN "
                   + "FOR r IN c LOOP "
                   + "UPDATE notice SET notino = r.new_no WHERE rowid = r.rowid; "
                   + "END LOOP; "
                   + "END;";
        jdbcTemplate.execute(sql);

        resetSequenceToMax();  // 최대 값으로 시퀀스를 리셋
    }

    public void resetSequenceToMax() {
        String sql = "SELECT MAX(notino) FROM notice";
        Integer maxId = jdbcTemplate.queryForObject(sql, Integer.class);

        if (maxId == null) {
            maxId = 0;
        }

        sql = "DROP SEQUENCE notice_seq";
        jdbcTemplate.execute(sql);

        sql = "CREATE SEQUENCE notice_seq START WITH " + (maxId + 1) + " INCREMENT BY 1";
        jdbcTemplate.execute(sql);
    }

    private static final class NoticeRowMapper implements RowMapper<NoticeVO> {
        @Override
        public NoticeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            NoticeVO noticeVO = new NoticeVO();
            noticeVO.setNotino(rs.getInt("notino"));
            noticeVO.setTitle(rs.getString("title"));
            noticeVO.setContent(rs.getString("content"));
            noticeVO.setNodate(rs.getTimestamp("nodate"));  // `getTimestamp` 메서드로 수정
            return noticeVO;
        }
    }
}

