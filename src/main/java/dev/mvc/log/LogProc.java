package dev.mvc.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service("dev.mvc.log.LogProc")
public class LogProc implements LogProcInter {

    @Autowired
    private LogDAOInter logDAO;
    
    @Autowired
    private JdbcTemplate jdbcTemplate; // JdbcTemplate 주입

    @Override
    public List<LogVO> getLogs(String userType) {
        return logDAO.getLogs(userType);
    }

    @Override
    public void insertLog(LogVO logVO) {
        String sql = "INSERT INTO log (logno, ip, logdate, memberno) VALUES (log_seq.NEXTVAL, ?, SYSDATE, ?)";
        jdbcTemplate.update(sql, logVO.getIp(), logVO.getMemberno());
    }
}
