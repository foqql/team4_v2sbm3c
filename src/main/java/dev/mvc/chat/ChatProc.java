package dev.mvc.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatProc implements ChatProcInter {
    @Autowired
    private ChatDAOInter chatDAO;

    @Override
    public int create(ChatVO chatVO) {
        return chatDAO.create(chatVO);
    }

    @Override
    public List<ChatVO> list() {
        List<ChatVO> allMessages = chatDAO.list();
        // 최신 15개의 메시지만 반환
        return allMessages.stream().sorted((a, b) -> b.getChatno() - a.getChatno()).limit(15).collect(Collectors.toList());
    }
}



