package dev.mvc.noticegood;

public interface NoticegoodDAOInter {

  public int notice_check(NoticegoodVO noticegoodVO);
  
  public int count_likes(int notino);
  
  public int noticegood_insert(NoticegoodVO noticegoodVO);
  
  public int noticegood_delete(NoticegoodVO noticegoodVO);
}
