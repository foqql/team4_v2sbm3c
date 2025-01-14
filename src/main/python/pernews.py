import os    # 폴더등 처리
import time  # sleep 지연
import json 
import cx_Oracle    # Oracle 입출력
import pandas as pd
from openai import OpenAI # 1.x~

def getConnection():
    conn = cx_Oracle.connect('team44/69017000@43.200.57.156:1521/XE')
    cursor = conn.cursor()
    
    return conn, cursor

conn, cursor = getConnection()

# pandas 출력 옵션 조정 (길이, 열 수 제한 해제)
pd.set_option('display.max_rows', None)  # 출력할 최대 행 수
pd.set_option('display.max_columns', None)  # 출력할 최대 열 수
pd.set_option('display.width', None)  # 출력 너비 제한 해제
pd.set_option('display.max_colwidth', None)  # 각 열의 최대 너비 제한 해제

# SQL 쿼리 실행
query = ''' 
SELECT newscrawlingno, title, content, rdate, source, url, genre
FROM newscrawling
WHERE rdate = (SELECT MAX(rdate) FROM newscrawling) and source = '개인'
'''

# pandas의 read_sql을 사용하여 결과를 DataFrame으로 가져오기
df = pd.read_sql(query, conn)

# 결과 출력
title = df.iloc[0]['TITLE']
print(title)

content_text = df.iloc[0]['CONTENT']


############################# 번역 및 데베 추가 ###################################3

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

cursor.execute("""
    UPDATE translate
    SET title = :title, content = :content
    WHERE rdate = (SELECT MAX(rdate) FROM newscrawling) and genre ='개인'
""", {'title': trans_title, 'content': trans_content})

conn.commit()

################################## 요약 및 데베 추가 ###################################3

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

cursor.execute("""
    UPDATE summary
    SET content = :content
    WHERE rdate = (SELECT MAX(rdate) FROM newscrawling) and genre ='개인'
""", {'content': sum_content})

conn.commit()

###################### 본문 데베 추가 #######################3

cursor.execute("""
    UPDATE news
    SET title = :title, content = :content, content2 = :content2, content3 = :content3, content4 = :content4, newsgenre = :newsgenre  
    WHERE rdate = (SELECT MAX(rdate) FROM newscrawling)
""", {'title': title, 'content': content_text, 'content2': trans_title, 'content3': trans_content, 'content4': sum_content, 'newsgenre': '개인'})

conn.commit()

# 크롤링 제목: title
# 크롤링 내용: content_text
# 번역 제목: trans_title
# 번역 내용: trans_content
# 요약 내용: sum_content