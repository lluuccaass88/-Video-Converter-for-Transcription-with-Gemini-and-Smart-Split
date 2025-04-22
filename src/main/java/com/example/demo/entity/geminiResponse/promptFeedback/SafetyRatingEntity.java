package com.example.demo.entity.geminiResponse.promptFeedback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SafetyRatingEntity {
  private String category;
  private String probability;

  @Override
  public String toString() {
    return "SafetyRating{" +
            "category='" + category + '\'' +
            ", probability='" + probability + '\'' +
            '}';
  }
}
