package nh.publy.backend.domain;

public class ReactionAddedEvent {
  private final Long storyId;
  private final Long reactionId;

  public ReactionAddedEvent(Long storyId, Long reactionId) {
    this.storyId = storyId;
    this.reactionId = reactionId;
  }

  public Long getStoryId() {
    return storyId;
  }

  public Long getReactionId() {
    return reactionId;
  }
}
