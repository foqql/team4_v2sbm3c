package dev.mvc.notice;

import java.util.List;

public interface NoticeProcInter {
  public int create(NoticeVO noticeVO);
  public NoticeVO read(int notino);
  public int update(NoticeVO noticeVO);
  public int delete(int notino);
  public List<NoticeVO> list();
  public void deleteAll();  // 추가
  public void resetSequence();  // 추가
  public void renumberNotices();  // 추가
  public int updateGood(int notino, int memberno);  
  
}

