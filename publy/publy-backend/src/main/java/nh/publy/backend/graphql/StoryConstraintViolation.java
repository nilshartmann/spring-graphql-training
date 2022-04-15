package nh.publy.backend.graphql;

public record StoryConstraintViolation(
  String message,
  StoryConstraintViolationField field) {
}
