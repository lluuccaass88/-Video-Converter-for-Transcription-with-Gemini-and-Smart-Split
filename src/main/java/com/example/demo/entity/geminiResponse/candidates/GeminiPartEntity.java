package com.example.demo.entity.geminiResponse.candidates;

import com.example.demo.entity.InlineDataEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiPartEntity {
  private String text;
  private InlineDataEntity inlineData;

  public GeminiPartEntity(String text) {
    this.text = text;
  }

  public GeminiPartEntity(InlineDataEntity inlineData) {
    this.inlineData = inlineData;
  }

  public GeminiPartEntity() {}

}