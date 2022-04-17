package nh.publy.backend.graphql;

import javax.validation.Path;

public enum StoryConstraintViolationField {
  title, body, tags, other;

  public static StoryConstraintViolationField forPropertyPath(Path propertyPath) {
    String fieldName = propertyPath.toString();

    if ("title".equals(fieldName)) {
      return title;
    }

    if ("bodyMarkdown".equals(fieldName)) {
      return body;
    }

    if ("tags".equals(fieldName)) {
      return tags;
    }

    return other;
  }
}
