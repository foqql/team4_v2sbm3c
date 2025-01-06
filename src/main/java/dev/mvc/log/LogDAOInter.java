package dev.mvc.log;

import dev.mvc.log.LogVO;

import java.util.List;

public interface LogDAOInter {
    List<LogVO> getLogs(String userType);
}
