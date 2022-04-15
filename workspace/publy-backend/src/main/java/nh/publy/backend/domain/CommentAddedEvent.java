package nh.publy.backend.domain;

public record CommentAddedEvent(Long storyId, Long commentId) {
}
