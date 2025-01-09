package dev.mvc.survey_good;
// survey, survey_good, member 테이블 조인
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SSdMVO {
  
    private int surveygoodno; // 요약된 기사 번호
    private String stopic; // join
    
    private String rdate; // 요약된 내용
    private int surveyno; // 요약된 내용
    private int memberno; // 요약된 내용
    private String id; // join
    private String mname; // join
    

   
}
