package com.example.demo.entity.geminiResponse.usageMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageMetadataEntity {
  private int promptTokenCount;
  private int candidatesTokenCount;
  private int totalTokenCount;

  @Override
  public String toString() {
    return "UsageMetadata{" +
            "promptTokenCount=" + promptTokenCount +
            ", candidatesTokenCount=" + candidatesTokenCount +
            ", totalTokenCount=" + totalTokenCount +
            '}';
  }
}
