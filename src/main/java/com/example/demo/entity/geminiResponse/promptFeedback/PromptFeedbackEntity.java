package com.example.demo.entity.geminiResponse.promptFeedback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PromptFeedbackEntity {
  private String blockReason;
  private List<SafetyRatingEntity> safetyRatings;

  @Override
  public String toString() {
    return "PromptFeedback{" +
            "blockReason='" + blockReason + '\'' +
            ", safetyRatings=" + safetyRatings +
            '}';
  }
}
