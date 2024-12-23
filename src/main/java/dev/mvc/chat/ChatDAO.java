package dev.mvc.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDAO implements ChatDAOInter {
    @Autowired
    private SqlSession sqlSession;

    private static String namespace = "dev.mvc.chat.Chat";

    @Override
    public int create(ChatVO chatVO) {
        return sqlSession.insert(namespace + ".create", chatVO);
    }

    @Override
    public List<ChatVO> list() {
        return sqlSession.selectList(namespace + ".list");
    }
    


}
