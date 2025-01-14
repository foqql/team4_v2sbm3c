package dev.mvc.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoticeProc implements NoticeProcInter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        int result = noticeDAO.delete(notino);
        if (result > 0) {
            noticeDAO.renumberNotices();
        }
        return result;
    }

    @Override
    public List<NoticeVO> list() {
        return noticeDAO.list();
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM notice";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void resetSequence() {
        noticeDAO.resetSequence();
    }

    @Override
    public void renumberNotices() {
        noticeDAO.renumberNotices();
    }

    @Override
    public int updateGood(int notino, int memberno) {
        String sql = "UPDATE notice SET good_count = good_count + 1 WHERE notino = ?";
        int result = jdbcTemplate.update(sql, notino);

        return result > 0 ? 1 : 0;
    }
}
