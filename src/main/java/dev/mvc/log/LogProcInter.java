package dev.mvc.log;

import java.util.List;

public interface LogProcInter {
    List<LogVO> getLogs(String userType);
    void insertLog(LogVO logVO);
    void deleteLog(int logno);  // 삭제 메서드 추가
}
