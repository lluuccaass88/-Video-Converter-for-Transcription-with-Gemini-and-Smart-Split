package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

//  @Value("${configurations.sicredi.services.query.base-url}")
//  private String consultaBaseUrl;

  @Bean
  public RestClient segurosConectaConsultaRestClient() {
    return RestClient.builder()
//            .baseUrl(consultaBaseUrl)
// .defaultHeader("Authorization", "Bearer SEU_TOKEN_AQUI") // Exemplo: Header padrão
            .build();
  }

}
