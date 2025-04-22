package com.example.demo.entity;

import com.example.demo.entity.geminiResponse.candidates.GeminiContentEntity;
import lombok.Data;

import java.util.List;

@Data
public class GeminiRequest {
  private List<GeminiContentEntity> contents;

  public GeminiRequest(List<GeminiContentEntity> contents) {
    this.contents = contents;
  }

}
