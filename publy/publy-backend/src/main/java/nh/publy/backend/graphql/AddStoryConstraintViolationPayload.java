package nh.publy.backend.graphql;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.Set;

public record AddStoryConstraintViolationPayload(Set<ConstraintViolation<?>> originalViolations) {
  public Collection<StoryConstraintViolation> getViolations() {
    return originalViolations.stream().map(v -> new StoryConstraintViolation(
      v.getMessage(),
      StoryConstraintViolationField.forPropertyPath(v.getPropertyPath())
    )).toList();
  }
}
