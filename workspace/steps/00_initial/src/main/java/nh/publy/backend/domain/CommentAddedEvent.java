package nh.publy.backend.domain;

public class CommentAddedEvent {
  private final Long storyId;
  private final Long commentId;

  public CommentAddedEvent(Long storyId, Long commentId) {
    this.storyId = storyId;
    this.commentId = commentId;
  }

  public Long getStoryId() {
    return storyId;
  }

  public Long getCommentId() {
    return commentId;
  }
}
