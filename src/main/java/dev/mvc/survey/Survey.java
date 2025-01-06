package dev.mvc.survey;

public class Survey {
  /** 페이지당 출력할 레코드 갯수 */
  public static int RECORD_PER_PAGE = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public static int PAGE_PER_BLOCK = 10;

  public static String getUploadDir() {
    String osName = System.getProperty("os.name").toLowerCase();
    String path = "";

    if (osName.contains("win")) { // Windows
      path = "C:\\kd\\deploy\\team4\\survey\\storage\\";
      // System.out.println("Windows: " + path);
    } else if (osName.contains("mac")) { // MacOS
      path = "/Users/yourusername/deploy/team4/survey/storage/";
      // System.out.println("MacOS: " + path);
    } else { // Linux
      path = "/home/ubuntu/deploy/team4/survey/storage/";
      // System.out.println("Linux: " + path);
    }

    return path;
  }
}
