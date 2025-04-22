package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenagerService {

  private final GeminiService geminiService;
  private final AudioService audioService;

  private static final String AUDIO_FILE_NAME = "/saida.mp4";
  private static final String VIDEO_FILE_NAME = "/vid.mp4";
  private static final String PREFIXO = "audio_parte_";
  private static final double DURACAO_MAXIMA = 40 * 60;

  @Value("${local.path}")
  private String localPath;

  /**
   * Gerência a execução da aplicação
   */
  public void menagerAplication() throws Exception {

    log.info("Extração do audio iniciada");

    log.info("Extração do audio concluida");

    audioService.extractAudio(localPath + VIDEO_FILE_NAME, localPath + AUDIO_FILE_NAME);

    List<byte []> filePathAudioList = new ArrayList<>();

    Double dutarionAudio = audioService.getMediaDurationSeconds(localPath + AUDIO_FILE_NAME);

    if(dutarionAudio < DURACAO_MAXIMA){
      filePathAudioList.add(audioService.readFileAudio(localPath + AUDIO_FILE_NAME));
    }else {
      List<String> audiosPart = audioService.splitAudio(localPath + AUDIO_FILE_NAME, localPath, PREFIXO, DURACAO_MAXIMA, dutarionAudio);

      for(String audioPart : audiosPart) {
        //TODO pensar em uma maneira de suavizar a transição de um audio para o outro, pois eles perdem o sentido quando a ia análisa o próximo.
        filePathAudioList.add(audioService.readFileAudio(audioPart));
      }
    }

    filePathAudioList.forEach(audio -> {
      try {
        geminiService.sendToGemini(audio);
      } catch (Exception e) {
        //TODO melhorar as exceptions
        throw new RuntimeException(e);
      }
    });

    log.info("Transcrição salva com sucesso!");

  }

}
