package com.example.demo.entity.geminiResponse;

import com.example.demo.entity.geminiResponse.candidates.CandidatesEntity;
import com.example.demo.entity.geminiResponse.promptFeedback.PromptFeedbackEntity;
import com.example.demo.entity.geminiResponse.usageMetadata.UsageMetadataEntity;
import com.example.demo.entity.geminiResponse.candidates.GeminiContentEntity;
import com.example.demo.entity.geminiResponse.candidates.GeminiPartEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponseEntity {

  private List<CandidatesEntity> candidates;
  private PromptFeedbackEntity promptFeedback;
  private UsageMetadataEntity usageMetadata;


  public Optional<String> getFirstCandidateText() {
    return Optional.ofNullable(candidates)
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(0))
            .map(CandidatesEntity::getContent)
            .map(GeminiContentEntity::getParts)
            .filter(parts -> !parts.isEmpty())
            .map(parts -> parts.get(0))
            .map(GeminiPartEntity::getText);
  }

  public String getFirstCandidateTextOrNull() {
    if (candidates != null && !candidates.isEmpty()) {
      CandidatesEntity firstCandidate = candidates.get(0);
      if (firstCandidate != null && firstCandidate.getContent() != null &&
              firstCandidate.getContent().getParts() != null && !firstCandidate.getContent().getParts().isEmpty()) {
        GeminiPartEntity firstPart = firstCandidate.getContent().getParts().get(0);
        if (firstPart != null) {
          return firstPart.getText();
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "GeminiResponse{" +
            "candidates=" + candidates +
            ", promptFeedback=" + promptFeedback +
            ", usageMetadata=" + usageMetadata +
            '}';
  }
}
