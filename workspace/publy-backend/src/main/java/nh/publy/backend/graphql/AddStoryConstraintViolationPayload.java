package nh.publy.backend.graphql;

import java.util.List;

public record AddStoryConstraintViolationPayload(List<AddStoryConstraintViolation> violations) {
}
