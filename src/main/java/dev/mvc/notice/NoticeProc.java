package dev.mvc.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoticeProc implements NoticeProcInter {

    @Autowired
    private JdbcTemplate jdbcTemplate; // JdbcTemplate 주입

    @Autowired
    private NoticeDAOInter noticeDAO;

    @Override
    public int create(NoticeVO noticeVO) {
        return noticeDAO.create(noticeVO);
    }

    @Override
    public NoticeVO read(int notino) {
        return noticeDAO.read(notino);
    }

    @Override
    public int update(NoticeVO noticeVO) {
        return noticeDAO.update(noticeVO);
    }

    @Override
    public int delete(int notino) {
        return noticeDAO.delete(notino); // DAO 호출
    }

    @Override
    public List<NoticeVO> list() {
        return noticeDAO.list();
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM notice";
        jdbcTemplate.execute(sql);  // JdbcTemplate을 사용하여 SQL 실행
    }

    @Override
    public void resetSequence() {
        noticeDAO.resetSequence();
    }

    @Override
    public void renumberNotices() {
        noticeDAO.renumberNotices();
    }

    // 추가된 메서드
    @Override
    public int updateGood(int notino, int memberno) {
        // 추천 수 증가 쿼리 작성
        String sql = "UPDATE notice SET good_count = good_count + 1 WHERE notino = ?";

        // JdbcTemplate을 사용하여 SQL 실행
        int result = jdbcTemplate.update(sql, notino);

        if (result > 0) {
            return 1;  // 추천 성공
        } else {
            return 0;  // 추천 실패
        }
    }
}
