package dev.mvc.exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class pycrw {
  public pycrw() {
    System.out.println("-> python crawling created.");
    //------------------------------------------------
    System.out.println("파이썬 크롤링 시작 ");
    try {
      // Python 스크립트 실행
      String pythonScriptPath = "C:/kd/ws_python/team4/exc.py";
      System.out.println("Running Python script at: " + pythonScriptPath);
      ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);

      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();
      
      // Python 스크립트의 출력 결과를 읽기
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
          System.out.println(line);
      }
      
      int exitCode = process.waitFor();
      System.out.println("Python script finished with exit code: " + exitCode);
  } catch (IOException | InterruptedException e) {
      e.printStackTrace();
  }
    System.out.println("파이썬 크롤링 끝");
    //------------------------------------------------
  }

}