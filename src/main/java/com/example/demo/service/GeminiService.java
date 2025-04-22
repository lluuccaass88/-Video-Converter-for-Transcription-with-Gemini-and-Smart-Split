package com.example.demo.service;

import com.example.demo.client.GeminiClient;
import com.example.demo.entity.geminiResponse.GeminiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

  private final GeminiClient geminiClient;


  private static final String TIPO_MIME_AUDIO = "audio/aac";
  private static final String PROMPT = "Esta audío se trata de uma hitória. Eu preciso que você transcreva a história para mim, mas de forma que a cada parte esteja em um tópico";
  private static final String TEXTO_FILE_NAME = "/video_descricao.txt";

  @Value("${local.path}")
  private String localPath;

  //TODO melhorar este prompt

  private String responseProcess(ResponseEntity<GeminiResponseEntity> response) throws IOException {
    Path filePath = Path.of(localPath + TEXTO_FILE_NAME);


    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      GeminiResponseEntity geminiResponse = response.getBody();
      Optional<String> textOptional = geminiResponse.getFirstCandidateText();

      if (textOptional.isPresent()) {

        Files.writeString(filePath, textOptional.get(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        log.debug(textOptional.get());

        return textOptional.get();
      } else if (geminiResponse.getPromptFeedback() != null
          && geminiResponse.getPromptFeedback().getBlockReason() != null) {
        String blockReason = "Resposta bloqueada. Razão: " + geminiResponse.getPromptFeedback().getBlockReason();
        log.error(blockReason);
        return blockReason;
      } else {
        String errorMsg = "A resposta da API não continha texto ou houve um problema inesperado na resposta.";
        log.error(errorMsg);
        return errorMsg;
      }
    } else {
      String errorMsg = "Erro na API: Status " + response.getStatusCode() + " - Body: " + response.getBody();
      log.error(errorMsg);
      return errorMsg;
    }
  }

  /**
   * Envia os arquivos de audio para a API do Gemini e processa o retorno escrevendo em um arquivo de texto
   *
   * @param filePathAudioList arquivo de audio em bytes para enviar a api do Gemini.
   */
  public void sendToGemini(byte[] filePathAudioList) throws Exception {

    try {
      ResponseEntity<GeminiResponseEntity> response = geminiClient.sendMediaToGmini(filePathAudioList, TIPO_MIME_AUDIO,
          PROMPT);
      responseProcess(response);
    } catch (IOException e) {
      log.error("Erro ao processar requisição: {}", filePathAudioList);
    }
  }
}
