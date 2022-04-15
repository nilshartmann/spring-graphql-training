package nh.publy.backend.domain;

public record CommentAddedEvent(Comment comment, Long storyId, Long commentId) {
}
