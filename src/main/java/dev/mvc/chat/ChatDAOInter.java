package dev.mvc.chat;

import java.util.List;
import java.util.Map;

public interface ChatDAOInter {
    public int create(ChatVO chatVO); // 새 채팅 추가
    public List<ChatVO> list(); // 채팅 목록 조회
    public int delete(int chatno); // 채팅 삭제
}