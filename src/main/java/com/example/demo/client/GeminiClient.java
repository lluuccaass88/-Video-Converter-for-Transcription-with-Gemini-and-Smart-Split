package com.example.demo.client;

import com.example.demo.entity.GeminiRequest;
import com.example.demo.entity.InlineDataEntity;
import com.example.demo.entity.geminiResponse.GeminiResponseEntity;
import com.example.demo.entity.geminiResponse.candidates.GeminiContentEntity;
import com.example.demo.entity.geminiResponse.candidates.GeminiPartEntity;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class GeminiClient {

  @Value("${api.gemini.key}")
  private String apiGeminiKey;

  @Value("${api.gemini.url}")
  private String apiGeminiUrl;
  
  private static HttpEntity<GeminiRequest> buildGeminiHttpEntity(String mimeType, String promptText, byte[] fileBytes) {

    String base64EncodedData = Base64.getEncoder().encodeToString(fileBytes);

    InlineDataEntity inlineData = new InlineDataEntity(mimeType, base64EncodedData);
    GeminiPartEntity mediaPart = new GeminiPartEntity(inlineData);
    GeminiPartEntity textPart = new GeminiPartEntity(promptText);

    GeminiContentEntity content = new GeminiContentEntity(Arrays.asList(mediaPart, textPart));
    content.setRole("user");

    GeminiRequest requestBody = new GeminiRequest(Collections.singletonList(content));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>(requestBody, headers);
  }

  /**
   * Envia um arquivo de mídia local (imagem, vídeo, áudio) para a API Gemini.
   *
   * @param fileBytes   Arquivo de audio em bytes.
   * @param mimeType   O MIME type correto do arquivo (ex: "audio/aac", "audio/mpeg", "image/jpeg").
   * @param promptText O prompt de texto a ser enviado junto com a mídia.
   * @return A resposta de texto da API Gemini, ou uma mensagem de erro.
   */
  public ResponseEntity<GeminiResponseEntity> sendMediaToGmini(byte[] fileBytes, String mimeType, String promptText) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    try {
      String url = UriComponentsBuilder.fromHttpUrl(apiGeminiUrl)
              .queryParam("key", apiGeminiKey)
              .toUriString();

      HttpEntity<GeminiRequest> entity = buildGeminiHttpEntity(mimeType, promptText, fileBytes);

      log.info("Enviando requisição para a API Gemini...");
      return restTemplate.postForEntity(url, entity, GeminiResponseEntity.class);

    } catch (Exception e) {
      String errorMsg = "Erro durante a chamada da API Gemini ou processamento: " + e.getMessage();
      System.err.println(errorMsg);
      if (e.getMessage() != null && e.getMessage().contains("413")) {
        System.err.println("--> Possível erro '413 Payload Too Large'. O arquivo é muito grande para `inlineData`.");
        System.err.println("--> Considere usar Google Cloud Storage (GCS).");
        throw new RuntimeException(errorMsg + " (Arquivo muito grande)");
      }
      throw new RuntimeException(errorMsg);
    }

  }

}

