package dev.mvc.exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class startProc_exc implements ApplicationRunner  {
  @Override
  public void run(ApplicationArguments args) throws Exception {
      System.out.println("-> 환율 크롤링 시작");
      
      
   // ------------------------------------------------------------------------------
      // Python 스크립트 실행 (추가된 부분)
      // ------------------------------------------------------------------------------
      try {
          String pythonScriptPath = "src/main/python/exc.py"; // Python 스크립트 경로
          ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
          processBuilder.redirectErrorStream(true);
          Process process = processBuilder.start();
          System.out.println("-> 환율 크롤링 중");
          // Python 스크립트의 출력 결과를 읽기
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
              System.out.println(line);
          }

          int exitCode = process.waitFor();
          System.out.println("Python script finished with exit code: " + exitCode);

          if (exitCode != 0) {
              System.out.println("Python script execution failed");
          }
      } catch (IOException | InterruptedException e) {
          e.printStackTrace();
      }
      
      System.out.println("-> 환율 크롤링 시작 끝");
      
  }
}