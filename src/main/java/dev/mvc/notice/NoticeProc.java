package dev.mvc.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoticeProc implements NoticeProcInter {

    @Autowired
    private JdbcTemplate jdbcTemplate; // JdbcTemplate 주입 추가

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
        return noticeDAO.delete(notino);
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
}
