package com.example.demo.entity.geminiResponse.candidates;

import com.example.demo.entity.geminiResponse.promptFeedback.SafetyRatingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidatesEntity {

  private GeminiContentEntity content;

  private String finishReason;
  private int index;
  private List<SafetyRatingEntity> safetyRatings;

  @Override
  public String toString() {
    return "GeminiCandidate{" +
            "content=" + content +
            ", finishReason='" + finishReason + '\'' +
            ", index=" + index +
            ", safetyRatings=" + safetyRatings +
            '}';
  }
}
