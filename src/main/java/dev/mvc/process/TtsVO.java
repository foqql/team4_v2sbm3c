package dev.mvc.process;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TtsVO {
  

    private int ttsno;       // TTS 번호
    
    private String audio;    // 음성 파일 경로
    
    private int newsno;      // 기사 번호

}