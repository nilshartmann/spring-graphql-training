package nh.publy.backend.graphql;

import nh.publy.backend.domain.CommentAddedEvent;
import nh.publy.backend.domain.ReactionAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Component
public class FeedbackPublisher {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Sinks.Many<FeedbackReceivedEvent> feedbackSink;

  public FeedbackPublisher() {
    this.feedbackSink = Sinks.many()
      .multicast()
      .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
  }

  @TransactionalEventListener
  public void onNewComment(CommentAddedEvent commentAddedEvent) {
    logger.trace("onNewComment received event for comment {}",
      commentAddedEvent.commentId()
    );

    this.feedbackSink.emitNext(
      new FeedbackReceivedEvent(
        NodeId.forStory(commentAddedEvent.storyId()),
        NodeId.forComment(commentAddedEvent.commentId())),
      Sinks.EmitFailureHandler.FAIL_FAST);
  }

  @TransactionalEventListener
  public void onNewReaction(ReactionAddedEvent reactionAddedEvent) {
    logger.trace("onNewReaction received event for reaction {}",
      reactionAddedEvent.reactionId()
    );

    this.feedbackSink.emitNext(
      new FeedbackReceivedEvent(
        NodeId.forStory(reactionAddedEvent.storyId()),
        NodeId.forReaction(reactionAddedEvent.reactionId())),
      Sinks.EmitFailureHandler.FAIL_FAST);
  }

  public Flux<FeedbackReceivedEvent> getFeedbackPublisher(NodeId storyId) {
    return this.feedbackSink
      .asFlux()
      .filter(event -> event.storyId().equals(storyId));
  }
}
