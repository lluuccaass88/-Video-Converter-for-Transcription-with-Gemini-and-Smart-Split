package com.example.demo.entity.geminiResponse.candidates;

import lombok.Data;
import java.util.List;

@Data
public class GeminiContentEntity {
  private List<GeminiPartEntity> parts;
  private String role = "user";

  public GeminiContentEntity(List<GeminiPartEntity> parts) {
    this.parts = parts;
  }
  public GeminiContentEntity() {}

}