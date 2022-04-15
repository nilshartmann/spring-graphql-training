package nh.publy.backend.graphql;

import nh.publy.backend.domain.CommentAddedEvent;
import nh.publy.backend.domain.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

/**
 * Emits a OnNewCommentEvent for each received application event
 * CommentAddedEvent
 */
@Component
public class CommentEventPublisher {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final CommentRepository commentRepository;

  private final Sinks.Many<OnNewCommentEvent> sink;

  public CommentEventPublisher(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
    this.sink = Sinks.many()
      .multicast()
      .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
  }

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void onNewComment(CommentAddedEvent event) {
    logger.trace("onNewComment received event for comment {}, currentSubscriberCount {}",
      event.commentId(),
      this.sink.currentSubscriberCount()
    );

    this.sink.emitNext(new OnNewCommentEvent(
      commentRepository,
      event.storyId(),
      event.commentId()
    ), Sinks.EmitFailureHandler.FAIL_FAST);
  }

  public Flux<OnNewCommentEvent> getPublisher(Long storyId) {
    return this.sink
      .asFlux()
      .filter(event -> storyId.equals(event.getStoryId()));
  }
}