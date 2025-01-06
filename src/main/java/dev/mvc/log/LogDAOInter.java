package dev.mvc.log;

import java.util.List;

public interface LogDAOInter {
    List<LogVO> getLogs(String userType);
    void deleteLog(int logno);  // 삭제 메서드 추가
}
