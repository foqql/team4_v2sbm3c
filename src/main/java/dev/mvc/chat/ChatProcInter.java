package dev.mvc.chat;

import java.util.List;

public interface ChatProcInter {
    public int create(ChatVO chatVO);
    public List<ChatVO> list();
}
