package dev.mvc.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Tool;

@Component
public class startProc implements ApplicationRunner  {
  @Override
  public void run(ApplicationArguments args) throws Exception {

      
      // ------------------------------------------------------------------------------
      // Python 스크립트 실행 (추가된 부분)
      // ------------------------------------------------------------------------------
//      try {
//          System.out.println("BBC크롤링");
//          String pythonScriptPath = "src/main/python/crawling_BBC.py"; // Python 스크립트 경로
//          ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
//          processBuilder.redirectErrorStream(true);
//          Process process = processBuilder.start();
//          // Python 스크립트의 출력 결과를 읽기
//          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//          String line;
//          while ((line = reader.readLine()) != null) {
//              System.out.println(line);
//          }
//
//          int exitCode = process.waitFor();
//          System.out.println("Python script finished with exit code: " + exitCode);
//
//          if (exitCode != 0) {
//              System.out.println("Python script execution failed");
//          }
//      } catch (IOException | InterruptedException e) {
//          e.printStackTrace();
//      }
            
      // ------------------------------------------------------------------------------
      // Python 스크립트 실행 (추가된 부분)
      // ------------------------------------------------------------------------------
//      try {
//        System.out.println("FOX크롤링");
//          String pythonScriptPath = "src/main/python/crawling_FOX.py"; // Python 스크립트 경로
//          ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
//          processBuilder.redirectErrorStream(true);
//          Process process = processBuilder.start();
//          // Python 스크립트의 출력 결과를 읽기
//          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//          String line;
//          while ((line = reader.readLine()) != null) {
//              System.out.println(line);
//          }
//
//          int exitCode = process.waitFor();
//          System.out.println("Python script finished with exit code: " + exitCode);
//
//          if (exitCode != 0) {
//              System.out.println("Python script execution failed");
//          }
//      } catch (IOException | InterruptedException e) {
//          e.printStackTrace();
//      }
            
      
  }
}