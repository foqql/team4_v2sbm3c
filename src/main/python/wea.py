import sys
import datetime
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import cx_Oracle
import time  # time 모듈을 추가합니다.

def getConnection():
    conn = cx_Oracle.connect('team44/69017000@43.200.57.156:1521/XE')
    cursor = conn.cursor()
    return conn, cursor

def load(driver, url):
    driver.get(url)

def crawling(continent, country, city):
    driver = webdriver.Chrome()
    driver.set_window_size(1800, 900)
    
    conn, cursor = getConnection()
    
    try:
        load(driver, 'https://wwwnew.kweather.co.kr/forecast/forecast_world_weather.html')

        # 대륙, 국가, 도시 선택
        continent_name = WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea1 > option:nth-child({int(continent)})'))).text
        WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea1 > option:nth-child({int(continent)})'))).click()
        time.sleep(0.5)  # 대기 시간 추가

        country_name = WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea2 > option:nth-child({int(country)})'))).text
        WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea2 > option:nth-child({int(country)})'))).click()
        time.sleep(0.5)  # 대기 시간 추가

        city_name = WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea3 > option:nth-child({int(city)})'))).text
        WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, f'#worldarea3 > option:nth-child({int(city)})'))).click()
        time.sleep(0.5)  # 대기 시간 추가

        # 검색 버튼 클릭
        WebDriverWait(driver, 10).until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, '#body_container > form:nth-child(2) > div > ul.world_area_select > li:nth-child(6) > input[type=image]'))).click()
   

        # area 테이블 업데이트
        area_update_sql = """
        UPDATE area
        SET continent = :1, country = :2, city = :3
        WHERE areano = (SELECT MAX(areano) FROM area)
        """
        cursor.execute(area_update_sql, (continent_name, country_name, city_name))
        conn.commit()

        soup = BeautifulSoup(driver.page_source, 'html.parser')

        # 데이터 추출
        weather = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_condition > span.world_present_forecast_weather')
        temp = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_condition > span.world_present_forecast_temp')
        windchill = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_condition > span.world_present_forecast_bodytemp')
        mintemp = soup.select_one('#body_container > div.world_week_forecast > ul > li.world_week_forecast_content > table > tbody > tr:nth-child(3) > td:nth-child(3) > span.lifestyle_min')
        maxtemp = soup.select_one('#body_container > div.world_week_forecast > ul > li.world_week_forecast_content > table > tbody > tr:nth-child(3) > td:nth-child(3) > span.lifestyle_max')
        humidity = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_table > table > tbody > tr > td:nth-child(1)')
        speed = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_table > table > tbody > tr > td:nth-child(3)')
        direction = soup.select_one('#body_container > div.world_present_forecast > ul.world_present_forecast_content > li.world_present_forecast_table > table > tbody > tr > td:nth-child(4)')
        udate = soup.select_one('#body_container > div:nth-child(4) > ul > li.world_navi_txt > span.world_navi_date')


        # 데이터 값 처리
        data = (
            weather.text.strip() if weather else '',
            temp.text.strip() if temp else '',
            windchill.text.strip() if windchill else '',
            mintemp.text.strip() if mintemp else '',
            maxtemp.text.strip() if maxtemp else '',
            humidity.text.strip() if humidity else '',
            speed.text.strip() if speed else '',
            direction.text.strip() if direction else '',
            udate.text.replace('·', '').replace('(현지시간)', '').strip() if udate else ''
        )

        # 데이터 삽입 SQL문
        insert_sql = """
        INSERT INTO weather (
            weatherno, memberno, classifyno, areano, weather, rdate, temp, windchill, mintemp, maxtemp, humidity, speed, direction, udate, passwd, recom
        ) VALUES (
            WEATHER_SEQ.NEXTVAL, 101, 6, (SELECT MAX(areano) FROM area), :2, TO_CHAR(SYSDATE, 'YYYY.MM.DD HH24:MI'), :3, :4, :5, :6, :7, :8, :9, TO_DATE(:10, 'YYYY.MM.DD HH24:MI'), '1234', 0
        )
        """
        
        cursor.execute(insert_sql, data)
        conn.commit()

    except Exception as e:
        print(f"오류 발생: {e}")

    finally:
        cursor.close()
        conn.close()
        driver.quit()

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("필수 인자가 부족합니다.")
        sys.exit(1)
    continent, country, city = sys.argv[1], sys.argv[2], sys.argv[3]
    crawling(continent, country, city)
