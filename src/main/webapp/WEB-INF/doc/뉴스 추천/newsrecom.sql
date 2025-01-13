DROP TABLE NEWSRECOM;

CREATE TABLE NEWSRECOM (
	newsrecomno 	NUMBER(10)		        NOT NULL,
	rdate	        DATE		            NOT NULL,
	newsno	        NUMBER(10)	DEFAULT 0	NOT NULL,
	memberno	    NUMBER(10)		        NOT NULL,
    FOREIGN KEY (memberno) REFERENCES member (memberno), -- 일정을 등록한 관리자 
    FOREIGN KEY (newsno) REFERENCES news (newsno) -- 일정을 등록한 관리자 
);

DROP SEQUENCE NEWSRECOM_SEQ;

CREATE SEQUENCE NEWSRECOM_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

select * from news;
select * from member;

-- 데이터 삽입
INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 1, 1);

INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 2, 1);

INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 2, 7);

INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 3, 9);

INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 5, 9);

-- 전체 목록
SELECT newsrecomno, rdate, newsno, memberno
FROM newsrecom
ORDER BY newsrecomno DESC;

5	2025-01-07 11:26:06	5	9
4	2025-01-07 11:26:06	3	9
3	2025-01-07 11:26:05	2	7
2	2025-01-07 11:26:04	2	1
1	2025-01-07 11:26:03	1	1


-- PK조회
SELECT newsrecomno, rdate, newsno, memberno
FROM newsrecom
WHERE newsrecomno = 1;

1	2025-01-07 10:53:01	2	1

-- newsno, memberno 로 조회
SELECT newsrecomno, rdate, newsno, memberno
FROM newsrecom
WHERE newsrecomno=7 and memberno=6;

-- 삭제
DELETE FROM newsrecom
WHERE newsrecomno = 2;
5	2025-01-07 11:26:06	5	9
4	2025-01-07 11:26:06	3	9
3	2025-01-07 11:26:05	2	7
1	2025-01-07 11:26:03	1	1
 
commit;

SELECT COUNT(*) as cnt 
FROM newsrecom
WHERE newsrecomno=2 and memberno=1;

SELECT COUNT(*) as cnt 
FROM newsrecom
WHERE newsrecomno=5 and memberno=9;

select * from news;
select * from member;
select * from newsrecom;

INSERT INTO NEWSRECOM(newsrecomno, rdate, newsno, memberno)
VALUES (NEWSRECOM_SEQ.nextval, sysdate, 23, 6);
commit;

select COUNT(*) as cnt
from newsrecom
where newsno = 23;

-- join 어느 설문을 누가 추천했는가?
-- 테이블 2개 join
SELECT c.newsrecomno, c.rdate, c.newsno, n.title, c.memberno
FROM news n, newsrecom c
WHERE n.newsno = c.newsno 
ORDER BY newsrecomno DESC;

-- 테이블 3개 join, as 사용 시 컬럼명 변경 가능 n.title as n_title
SELECT c.newsrecomno, c.rdate, c.newsno, n.title as n_title, c.memberno, m.id, m.mname
FROM news n, newsrecom c, member m
WHERE n.newsno = c.newsno and c.memberno = m.memberno
ORDER BY newsrecomno DESC;

DELETE FROM news
WHERE newsno = 18;
commit;


-- integrity constraint (TEAM4.SYS_C0010027) violated - child record found
-- newsrecom의 제약조건 SYS_C0010027에 delete rule - cascade 추가
ALTER TABLE NEWSRECOM
DROP CONSTRAINT SYS_C0010027; 

ALTER TABLE NEWSRECOM
ADD CONSTRAINT SYS_C0010027 FOREIGN KEY (newsno)
REFERENCES news(newsno) 
ON DELETE CASCADE;