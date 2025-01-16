import re
import warnings
warnings.filterwarnings(action='ignore')
import sys
import os    # 폴더등 처리
import logging
logging.basicConfig(filename='app.log', level=logging.INFO, encoding='utf-8')
import time  # sleep 지연

import pandas as pd # Excel과 비슷한 구조를 지원하는 데이터 분석 패키지
import cx_Oracle    # Oracle 입출력
from sqlalchemy import create_engine # Pandas -> Oracle

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager

from bs4 import BeautifulSoup
from urllib.request import urlopen
from urllib.request import urlretrieve
from urllib.parse import quote
from urllib.error import HTTPError

#####
from flask import Flask
from apscheduler.schedulers.background import BackgroundScheduler

# Flask 애플리케이션 생성
app = Flask(__name__)
#####

# 웹페이지 로딩을 완료시까지 기다림. 기본값: 0.5초
def load(url, second=0.5): # Selenium으로 태그 검색
    try:
        driver.get(url) # url 접속
        time.sleep(second) # 웹페이지 로딩을 완료시까지 기다림. 0.5: 0.5초
    except HTTPError as e:
        print(e)
        return None

def loadbs(url, second=0.5): # url load후 BeautifulSoup으로 태그 검색
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

def str2num(src):
    num = src.strip()                 # 공백 제거
    num = re.sub(r'[^0-9.]', '', num) # 순수 숫자로 변경
    return int(num)

def getConnection():
    conn = cx_Oracle.connect('team44/69017000@43.200.57.156:1521/XE')
    cursor = conn.cursor()
    
    return conn, cursor

conn, cursor = getConnection()


# try:
#   cursor.execute('DROP TABLE visit') # 존재하면 삭제됨
# except Exception as e:    
#    print(e)
    # pass

# Options 클래스의 인스턴스를 생성합니다.
options = Options()
# 브라우저 창이 보이지 않도록 헤드리스 모드 활성화
# options.add_argument('--headless')
# Chrome 브라우저 창이 즉시 닫히는 것을 방지합니다.
options.add_experimental_option('detach', True)
# 불필요한 콘솔 메시지를 제거합니다.
options.add_experimental_option('excludeSwitches', ['enable-logging'])

# Service 객체를 생성하고 ChromeDriverManager를 통해 드라이버를 설치합니다.
driver_path = ChromeDriverManager().install()
correct_driver_path = os.path.join(os.path.dirname(driver_path), "chromedriver.exe")
# Chrome 브라우저를 실행하는 ChromeDriver 객체를 생성합니다.
driver = webdriver.Chrome(service=Service(executable_path=correct_driver_path), options=options)

driver.set_window_size(900, 900) # width, height


def crawling_FOX():
  ####################### 크롤링 ##########################
    
    load('https://www.foxnews.com/', 1)
    
    
    driver.find_element(By.CSS_SELECTOR, '#wrapper > div.page > div.region-content-sidebar > main > div.big-top > div > article > div.m > a > picture > img').click()            
                                         
    
    # 두 가지 CSS 선택자 중 하나가 존재하면 클릭하기
    # try:
    #     driver.find_element(By.CSS_SELECTOR, '#main-content > article > section:nth-child(1) > div > div.sc-e70150c3-0.fbvxoY > div.sc-93223220-0.bOZIBp > div:nth-child(3) > div > div > div > a > div > div.sc-6781995d-5.bmQwDh > div.sc-8ea7699c-1.hxRodh > div > h2').click()   
    # except:
    #     try:
    #         driver.find_element(By.CSS_SELECTOR, '#main-content > article > section:nth-child(1) > div > div.sc-e70150c3-0.fbvxoY > div.sc-93223220-0.bOZIBp > div:nth-child(3) > div > div > div > a > div > div.sc-6781995d-5.dWflPh > div.sc-8ea7699c-1.hxRodh > div > h2').click()        
    #     except:
    #         try:
    #             driver.find_element(By.CSS_SELECTOR, '#main-content > article > section:nth-child(2) > div > div.sc-e70150c3-0.fbvxoY > div.sc-93223220-0.bOZIBp > div:nth-child(3) > div > div > div > a > div > div.sc-6781995d-5.bmQwDh > div.sc-8ea7699c-1.hxRodh > div > h2').click()                                           
    #         except:
    #             print("세 가지 선택자 모두 찾을 수 없습니다.")
    
    query = ''' 
    select url
    from newscrawling
    where rdate =(SELECT MAX(rdate) FROM newscrawling where source = 'FOX')
    '''
    
    df = pd.read_sql(query, conn)
    
    # 결과 출력
    last_url = str(df.iloc[0]['URL']).strip()
    url = driver.current_url.strip()  
    
    
    if last_url == url:
        print(last_url)
        print(url)
        print("URL이 같습니다. 크롤링 취소")
          
    else:
        print(last_url)
        print(url)
        print("URL이 다릅니다. 크롤링 진행")
        bs=getbs()
        
        title = bs.select('#wrapper > div.page-content > div.row.full > main > article > header > div.article-meta.article-meta-upper > h1')[0].text
                        
        print(f'제목: {title}')
        print('내용')
        
        # 'data-component="text-block"'인 div 태그만 선택
        content = bs.select('div[class="article-body"] p:not(.dek):not(.copyright):not(.author-bio)')
        content_text = ""
        
        for paragraph in content:
          # 특정 단어들이 포함된 경우 출력하지 않기
            if any(keyword in paragraph.text for keyword in 
              ["FOX", 
              "fox",
              "Fox",
              ".com", 
              ".COM"]):  
              continue
          
            content_text += paragraph.text + "\n"
        
        print(content_text)
        
        cursor.execute("""
          INSERT INTO newscrawling (newscrawlingno, title, content, rdate, source, url, genre) 
          VALUES (newscrawling_seq.NEXTVAL, :title, :content, sysdate, 'FOX', :url, 'main')
        """, {'title': title, 'content': content_text, 'url': url})
        
        conn.commit()
        
        ############################## 번역 및 데베 추가 ###################################3
                  
        import os
        import time
        import json 
        
        from openai import OpenAI # 1.x~
        
        client = OpenAI(api_key=os.getenv('OPENAI_API_KEY'))
        
        prompt = f'{title}\n\n{content_text} 한국어로 번역해줘. 출력할 때, 제목과 기사내용만 출력해줘. 기사 내용은 적절한 곳에서 문단으로 나눠줘. 출력 예시는 제목: ,기사내용: 이런 식으로 출력해주고 리스트 형태인 대괄호는 쓰지 않고 출력해줘. ';
        
        response = client.chat.completions.create( # 1.x
            model="gpt-4o-mini-2024-07-18",
            messages=[
              {
                  'role': 'system',
                  'content': '너는 한국어 번역기야'
              },
              {
                  'role': 'user',
                  'content': prompt + '\n\n출력 형식: JSON으로 출력해줘.' 
              }
            ],
            n=1,             # 응답수, 다양한 응답 생성 가능
            max_tokens=4096,  # 응답 생성시 최대 1000개의 단어 사용
            temperature=0,   # 창의적인 응답여부, 값이 클수록 확률에 기반한 창의적인 응답이 생성됨
            response_format= { "type":"json_object" }
        )
        
        # print(response.choices[0].message.content)
        translated_data = json.loads(response.choices[0].message.content)
        
        # 제목과 내용을 각각 출력
        trans_title = translated_data["제목"]
        trans_content = translated_data["기사내용"]
        
        # 출력
        print("번역된 제목:", trans_title)
        print("번역된 내용:", trans_content)
        
        # INSERT 쿼리 실행
        cursor.execute("""
          INSERT INTO translate (translateno, title, content) 
          VALUES (translate_seq.NEXTVAL, :title, :content)
        """, {'title': trans_title, 'content': trans_content})
        
        conn.commit()
        
        ################################## 요약 및 데베 추가 ###################################3
        
        
        import os
        import time
        import json 
        
        from openai import OpenAI # 1.x~
        
        client = OpenAI(api_key=os.getenv('OPENAI_API_KEY'))
        
        prompt = f'{trans_content} 이게 기사내용인데 요약해서 한 문단으로 써줄래. 출력 예시는 요약: 이런 식으로 출력해줘.';
        
        response = client.chat.completions.create( # 1.x
            model="gpt-4o-mini-2024-07-18",
            messages=[
              {
                  'role': 'system',
                  'content': '너는 요약 시스템이야'
              },
              {
                  'role': 'user',
                  'content': prompt + '\n\n출력 형식: JSON으로 출력해줘.' 
              }
            ],
            n=1,             # 응답수, 다양한 응답 생성 가능
            max_tokens=4096,  # 응답 생성시 최대 1000개의 단어 사용
            temperature=0,   # 창의적인 응답여부, 값이 클수록 확률에 기반한 창의적인 응답이 생성됨
            response_format= { "type":"json_object" }
        )
        
        summary_data = json.loads(response.choices[0].message.content)
        
        # 제목과 내용을 각각 출력
        sum_content = summary_data["요약"]
        
        # 출력
        print("요약된 내용:", sum_content)
        
        # INSERT 쿼리 실행
        cursor.execute("""
          INSERT INTO summary (summaryno, content) 
          VALUES (summary_seq.NEXTVAL, :content)
        """, {'content': sum_content})
        
        conn.commit()
        
        ###################### 본문 데베 추가 #######################
        # INSERT 쿼리 실행
        cursor.execute("""
          INSERT INTO news (
          newsno, classifyno, memberno, newscrawlingno, translateno, summaryno, rdate, newsgenre, title, content, content2) 
          VALUES (
          news_seq.NEXTVAL, 3, 1, 
          (SELECT MAX(newscrawlingno) FROM newscrawling), 
          (SELECT MAX(translateno) FROM translate),
          (SELECT MAX(summaryno) FROM summary), 
          sysdate, 'FOX', :title, :content, :content2)
        """, {'title': trans_title, 'content': trans_content, 'content2': sum_content})
        
        conn.commit()
        
        # 크롤링 제목: title
        # 크롤링 내용: content_text
        # 번역 제목: trans_title
        # 번역 내용: trans_content
        # 요약 내용: sum_content
        

#########
# APScheduler로 주기적인 작업 설정
def start_scheduler():
    scheduler = BackgroundScheduler()
    scheduler.add_job(crawling_FOX, 'interval', seconds=100)  # 1분마다 작업 실행
    scheduler.start()
    
# Flask 라우트 예시
@app.route('/')
def home():
    return "Flask 크롤러가 작동 중입니다."

if __name__ == '__main__':
    start_scheduler()  # 스케줄러 시작
    app.run(debug=True)
##############