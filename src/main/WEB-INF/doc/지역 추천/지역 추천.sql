CREATE TABLE areagood (
	areagoodno	NUMBER(10)	NOT NULL,
	rdate	DATE	NOT NULL,
	weatherno	NUMBER(10)	NOT NULL,
	memberno	NUMBER(10)	NOT NULL,
    FOREIGN KEY (weatherno) REFERENCES weather (weatherno),
    FOREIGN KEY (memberno) REFERENCES member (memberno)
);

-- ALTER TABLE areagood ADD CONSTRAINT PK_AREAGOOD PRIMARY KEY (areagoodno);

DROP TABLE areagood;

DROP SEQUENCE areagood_seq;

CREATE SEQUENCE areagood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
INSERT INTO areagood(areagoodno, rdate, weatherno, memberno)
VALUES (areagood_seq.nextval, '2024-12-24', 1, 1);

INSERT INTO areagood(areagoodno, rdate, weatherno, memberno)
VALUES (areagood_seq.nextval, '2024-12-24', 1, 6);

INSERT INTO areagood(areagoodno, rdate, weatherno, memberno)
VALUES (areagood_seq.nextval, '2024-12-24', 1, 7);

INSERT INTO areagood(areagoodno, rdate, weatherno, memberno)
VALUES (areagood_seq.nextval, '2024-12-24', 1, 9);



    SELECT areagoodno, rdate, weatherno, memberno
    FROM areagood
    ORDER BY areagoodno DESC;




COMMIT;

-- 전체 목록
SELECT areagoodno, rdate, weatherno, memberno
FROM areagood
ORDER BY areagoodno DESC;


-- 조회
SELECT areagoodno, rdate, weatherno, memberno
FROM areagood
WHERE areagoodno = 1;

-- 삭제
DELETE FROM areagood
WHERE areagoodno = 1;

SELECT COUNT(*) as cnt
FROM areagood
WHERE weatherno=1 AND memberno=1;


SELECT COUNT(*) as cnt
FROM areagood
WHERE weatherno=2 AND memberno=6;



-- 추천
UPDATE weather
SET recom = recom + 1
WHERE weatherno = 1;



-- 비추천
UPDATE weather
SET recom = recom - 1
WHERE weatherno = 1;



select * from areagood;






CALENDARNO LABELDATE  LABEL                               SEQNO
---------- ---------- ------------------------------ ----------
         1 2024-12-24 크리스마스 이브                         1
         2 2024-12-25 휴강 안내                               1
         3 2024-12-25 학원 출입 안내                          2

-- 특정 날짜의 목록
SELECT calendarno, labeldate, label, seqno
FROM calendar
WHERE labeldate = '2025-01-01';

CALENDARNO LABELDATE  LABEL                                                   SEQNO
---------- ---------- -------------------------------------------------- ----------
         1 2024-12-24 크리스마스 이브                                             1

-- 조회수 증가
UPDATE calendar
SET cnt = cnt + 1
WHERE calendarno = 1;

-- 조회
SELECT calendarno, labeldate, label, title, content, cnt, regdate, seqno
FROM calendar
WHERE calendarno = 1;

-- 변경
UPDATE calendar
SET labeldate = '', label = '', title = '', content = '', seqno = 1
WHERE calendarno = 1;

-- 삭제
DELETE FROM calendar
WHERE calendarno = 7;

commit;

-------------------------------------------------------------------------------------------


-- JOIN

-- 테이블 2개 join
SELECT r.areagoodno, r.rdate, r.weatherno, c.weather, r.memberno
FROM weather c, areagood r
where c.weatherno = r.weatherno
ORDER BY areagoodno DESC;


-- 테이블 3개 join
SELECT r.areagoodno, r.rdate as r_rdate, r.weatherno, c.weather, r.memberno, m.id, m.mname
FROM weather c, areagood r, member m
where c.weatherno = r.weatherno AND r.memberno = m.memberno
ORDER BY areagoodno DESC;



















