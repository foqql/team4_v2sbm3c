package dev.mvc.log;

import java.util.List;

public interface LogProcInter {
    List<LogVO> getLogs(String userType);
    void insertLog(LogVO logVO); // 메서드 추가
}
