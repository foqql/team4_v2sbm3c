# 임포트 & 차트
import warnings
warnings.filterwarnings(action='ignore')

import os    # 폴더등 처리
from datetime import datetime
import time  # sleep 지연
import sys
sys.stdout.reconfigure(encoding='utf-8')


import pandas as pd # Excel과 비슷한 구조를 지원하는 데이터 분석 패키지
import cx_Oracle    # Oracle 입출력
from sqlalchemy import create_engine # Pandas -> Oracle

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
# from webdriver_manager.chrome import ChromeDriverManager

from bs4 import BeautifulSoup
from urllib.request import urlopen
from urllib.request import urlretrieve
from urllib.parse import quote
from urllib.error import HTTPError


import sqlite3
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib import font_manager, rc
#####
from flask import Flask
from apscheduler.schedulers.background import BackgroundScheduler

# Flask 애플리케이션 생성
app = Flask(__name__)
#####

# 한글 폰트 설정 (예: 'Malgun Gothic' 사용)
font_path = 'C:/Windows/Fonts/malgun.ttf'  # 윈도우에서의 폰트 경로
font_name = font_manager.FontProperties(fname=font_path).get_name()
rc('font', family=font_name)

options = Options()
options.add_argument('--headless')

def crawl_and_insert_data():
    # 웹페이지 로딩을 완료시까지 기다림. 기본값: 0.5초
    def load(url, second=0.0): # Selenium으로 태그 검색
        try:
            driver.get(url) # url 접속
            time.sleep(second) # 웹페이지 로딩을 완료시까지 기다림. 0.5: 0.5초
        except HTTPError as e:
            print(e)
            return None

    def loadbs(url, second=0.0): # url load후 BeautifulSoup으로 태그 검색
        try:
            driver.get(url) # url 접속
            time.sleep(second) # 웹페이지 로딩을 완료시까지 기다림. 0.5: 0.5초
            bs = BeautifulSoup(driver.page_source, 'html.parser')
        except HTTPError as e:
            print(e)
            return None
        else:
            return bs # 정상 처리  
        
    def getbs(): # BeautifulSoup으로 태그 검색
        try:
            bs = BeautifulSoup(driver.page_source, 'html.parser')
        except HTTPError as e:
            print(e)
            return None
        else:
            return bs # 정상 처리  

    def getConnection():
        conn = cx_Oracle.connect('team44/69017000@43.200.57.156:1521/XE')
        cursor = conn.cursor()
        
        return conn, cursor

    conn, cursor = getConnection()

    # Options 클래스의 인스턴스를 생성합니다.
    #options = Options()
    # Chrome 브라우저 창이 즉시 닫히는 것을 방지합니다.
    #options.add_experimental_option('detach', True)
    # 불필요한 콘솔 메시지를 제거합니다.
    #options.add_experimental_option('excludeSwitches', ['enable-logging'])

    # Service 객체를 생성하고 ChromeDriverManager를 통해 드라이버를 설치합니다.
    # service = Service(ChromeDriverManager().install())

    # Chrome 브라우저를 실행하는 ChromeDriver 객체를 생성합니다.
    # driver = webdriver.Chrome()
    # driver.set_window_size(1600, 900) # width, height

    # # exchange_crawling_input -> 환율 크롤링 테이블 입력
    # def exchange_crawling_input(currency, source, value): # 통화명, 매매 기준율, 등락율
    #     # INSERT 쿼리 실행
    #     query = ''' 
    #     INSERT INTO exchange_crawling (crawlingno, currency, source, value, rdate)
    #     VALUES (exchange_crawling_seq.NEXTVAL, :currency, :source, :value, SYSDATE)
    #     '''
    #     # 바인딩할 변수 딕셔너리 정의
    #     params = {
    #         'currency': currency,
    #         'source': source,
    #         'value': value
    #     }
        
    #     # 쿼리 실행 시 바인딩된 변수 전달
    #     cursor.execute(query, params)
    #     conn.commit()
        
    # exchange_input -> 환율 테이블 입력
    def exchange_input(source, yday, value, currency,classifyno, map): # 매매 기준율, 전일대비, 등락률, 통화명
        print("exchange_input - > "+ currency )
        query = ''' 
        INSERT INTO exchange (
            exchangeno, classifyno, name, price, krw, value, yesterday, recent, map
        ) VALUES (
            exchange_seq.NEXTVAL, :classifyno, :name, :price, :krw, :value, :yesterday, SYSDATE, :map
        )
        '''
        # source가 문자열이면 쉼표를 제거하고 실수로 변환
        if isinstance(source, str):
            source = float(source.replace(',', ''))  # 쉼표 제거 후 실수로 변환
        if isinstance(yday, str):
            # yday = yday.replace('▲', '').strip()
            yday = yday.replace('▲', '').replace('▼', '').strip()# '▲' 기호 제거하고 공백 및 줄바꿈 제거
            yday = float(yday)  # 실수로 변환
        yesterday = source - yday

        params = {
            'classifyno': classifyno,  # 해당 카테고리 번호
            'name': currency,  # 'name' 키에 해당하는 값
            'price': source,  # 'price' 키에 해당하는 값 (예: 1450.50)
            'krw': 1/source,  # 원화 가격 계산
            'value': value,  # 변동 값
            'yesterday': yesterday,  # 어제 가격에서 오늘 가격 빼기
            'map' : map
        }
        
        # 쿼리 실행
        cursor.execute(query, params)
        
        # 변경사항 커밋
        conn.commit()



    # 차트 데이터 추출
    def select_chart(conn, country):
        select_query = '''
            SELECT price, recent
            FROM exchange
            WHERE name LIKE :1
            ORDER BY recent DESC
        '''
        df = pd.read_sql_query(select_query, conn, params=(f'%{country}%',))
        return df

    # 차트 png(사진) 데이터베이스에 저장
    def chart(country):
        # 데이터를 가져옵니다.
        df = select_chart(conn, country)
        
        # 날짜 열을 datetime 형식으로 변환
        df['recent'] = pd.to_datetime(df['RECENT'])
        
        # 점과 선 차트를 그립니다.
        plt.figure(figsize=(10, 6))
        plt.plot(df['recent'], df['PRICE'], marker='o', markersize=5, linestyle='-', color='blue')

        # 일정 간격으로만 값을 표시합니다.
        interval = 15  # 몇 번째 점마다 값을 표시할지 설정
        for i in range(0, len(df), interval):
            plt.text(df['recent'][i], df['PRICE'][i], f'{df["PRICE"][i]:.2f}', fontsize=10, ha='right')
        
        # 차트 제목과 축 레이블을 설정합니다.
        plt.title('환율 변동 추이 (' + country + ')')
        plt.xlabel('날짜')
        plt.ylabel('환율')
        
        # 그리드 추가
        plt.grid(True)
        
        # x축의 날짜 형식을 설정합니다.
        plt.gcf().autofmt_xdate()

        # 차트를 파일로 저장합니다. (파일 경로를 받아서 저장)
        base_dir = '/kd/deploy/team4/exchange/storage'
        os.makedirs(base_dir, exist_ok=True)  # 경로가 없으면 생성
        chart_filename = f'exchange_rate_chart_{country}_{datetime.now().strftime("%Y%m%d%H%M%S")}.png'
        chart_filepath = os.path.join(base_dir, chart_filename)

        # 차트를 파일로 저장
        plt.savefig(chart_filepath)
        plt.close()

        # 차트 파일 크기 확인
        chart_size = os.path.getsize(chart_filepath)  # 파일 크기 (바이트 단위)
        print(f"차트 파일 크기: {chart_size / 1024:.2f} KB")  # KB 단위로 출력
        
        return chart_filepath, chart_size  # 이미지 경로 반환




    def save_chart_to_db(conn, country):
        # 차트를 그리고 파일 경로를 받기
        chart_filepath, chart_size = chart(country)

        print(f"차트 파일이 저장되었습니다: {chart_filepath}")
        
        # 데이터베이스 업데이트
        update_query = """
            UPDATE exchange
            SET file1 = :chart_filename, FILE1SAVED = :chart_filename, size1 = :chart_size
            WHERE exchangeno = (SELECT max(exchangeno)
            FROM (
                SELECT exchangeno,name
                FROM (
                    SELECT * 
                    FROM exchange
                    ORDER BY recent DESC
                ) 
                WHERE ROWNUM <= 3 
                ORDER BY exchangeno)
            WHERE name like :country)
        """
        
        params = {
            'chart_filename': os.path.basename(chart_filepath),  # 파일명만 저장
            'country': f'%{country}%',  # 'country' 변수에 따라 부분 일치 가능
            'chart_size': chart_size
        }
        
        cursor.execute(update_query, params)
        conn.commit()


    # 파일 삭제(진행 중)
    def delete_chart_file(filepath):
            # 파일 이름 및 경로 설정
        base_dir = '/kd/deploy/team4/exchange/storage'
        os.makedirs(base_dir, exist_ok=True)  # 경로가 없으면 생성
        chart_filename = f'exchange_rate_chart_{country}_{datetime.now().strftime("%Y%m%d%H%M%S")}.png'
        chart_filepath = os.path.join(base_dir, chart_filename)
        
        """차트 파일을 삭제하는 함수"""
        if os.path.exists(filepath):
            os.remove(filepath)
            print(f"Chart file {filepath} has been deleted.")
        else:
            print(f"Chart file {filepath} does not exist.")

    # 크롤링 함수 & 인풋
    def usd_input():
        #미국 달러 데이터 추출
        usd = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(1) > td:nth-child(2)')
        source = usd[0].get_text(strip=True)
        print("source " + source)
        # 전일대비 추출
        usd = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(1) > td:nth-child(3)')
        yday = usd[0].get_text(strip=True)
        print("yday " + yday)
        # 등락율
        usd = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(1) > td:nth-child(4)')
        value = usd[0].get_text(strip=True)
        print("value " + value)
        #통화명
        usd = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(1) > th > a')
        currency = usd[0].get_text(strip=True)
        print("currency " + currency)

        map = '''<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d243306.59702663144!2d-77.28381806605537!3d38.8828153767403!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x89b7c6de5af6e45b%3A0xc2524522d4885d2a!2z66-46rWtIOybjOyLse2EtCBEQyDsm4zsi7HthLQgRC5DLg!5e0!3m2!1sko!2skr!4v1735538050502!5m2!1sko!2skr" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>'''
    # exchange_crawling_input(currency, source, value)
        exchange_input(source, yday, value, currency, 1, map)
        save_chart_to_db(conn, currency)





    def jpy_input():
        # 일본 엔 데이터 추출
        jpy = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(2) > td:nth-child(2)')
        source = jpy[0].get_text(strip=True)
        print(source)

        jpy = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(2) > td:nth-child(3)')
        yday = jpy[0].get_text(strip=True)
        print(yday)

        jpy = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(2) > td:nth-child(4)')
        value = jpy[0].get_text(strip=True)
        print(value)
        
        jpy = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(2) > th > a')
        currency = jpy[0].get_text(strip=True)
        print(currency)

        map = '''<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d73349.01503932607!2d139.7830676004459!3d35.662247212932286!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x605d1b87f02e57e7%3A0x2e01618b22571b89!2z7J2867O4IOuPhOy_hOuPhA!5e0!3m2!1sko!2skr!4v1735538125368!5m2!1sko!2skr" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>'''
    #  exchange_crawling_input(currency, source, value)
        exchange_input(source, yday, value, currency, 12, map)
        save_chart_to_db(conn, currency)
        

    def gbp_input():
        # 영국 파운드 데이터 추출
        gbp = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(6) > td:nth-child(2)')
        source = gbp[0].get_text(strip=True)
        print(source)

        gbp = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(6) > td:nth-child(3)')
        yday = gbp[0].get_text(strip=True)
        print(yday)

        gbp = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(6) > td:nth-child(4)')
        value = gbp[0].get_text(strip=True)
        print(value)
        
        gbp = bs.select('body > div.ct.page-exchange > div.container > div:nth-child(2) > div.table-wrap-outline.mt-3 > table > tbody > tr:nth-child(6) > th > a')
        currency = gbp[0].get_text(strip=True)
        print(currency)

        map = '''<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d158858.58518354257!2d-0.26640247157192015!3d51.528526204716215!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x47d8a00baf21de75%3A0x52963a5addd52a99!2z7JiB6rWtIOufsOuNmA!5e0!3m2!1sko!2skr!4v1735538150375!5m2!1sko!2skr" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>'''
    #  exchange_crawling_input(currency, source, value)
        exchange_input(source, yday, value, currency, 11, map)
        save_chart_to_db(conn, currency)


    # def sel(conn): # 환율 크롤링 테이블 조회
    #     # 최근 3개의 데이터 조회 쿼리
    #     select_query = '''
    #     SELECT * 
    #     FROM (
    #         SELECT * 
    #         FROM exchange_crawling
    #         ORDER BY rdate DESC
    #     ) 
    #     WHERE ROWNUM <= 3
    #     ORDER BY CRAWLINGNO
    #     '''
        
    #     # 쿼리 실행
    #     df = pd.read_sql(select_query, conn)
        
    #     # 결과 출력
    #     print(df)


    def select(conn): # 환율 테이블 조회
            # 최근 3개의 데이터 조회 쿼리
        select_query = '''
        SELECT * 
        FROM (
            SELECT * 
            FROM exchange
            ORDER BY recent DESC
        ) 
        WHERE ROWNUM <= 3
        ORDER BY exchangeno
        '''
        
        # 쿼리 실행
        df = pd.read_sql(select_query, conn)
        
        # 결과 출력
        print(df)



    # 크롬 열기
    driver = webdriver.Chrome()
    driver.set_window_size(1600, 900) # width, height
    # 환율 크롤링 페이지로 접속
    load('https://www.kita.net/cmmrcInfo/ehgtGnrlzInfo/rltmEhgt.do')
    bs = getbs()

    usd_input()
    jpy_input()
    gbp_input()

#########
# APScheduler로 주기적인 작업 설정
def start_scheduler():
    scheduler = BackgroundScheduler()
    scheduler.add_job(crawl_and_insert_data, 'interval', seconds=300)  # 1분마다 작업 실행
    scheduler.start()
    
# Flask 라우트 예시
@app.route('/')
def home():
    return "Flask 크롤러가 작동 중입니다."

if __name__ == '__main__':
    start_scheduler()  # 스케줄러 시작
    app.run(debug=True)
##############