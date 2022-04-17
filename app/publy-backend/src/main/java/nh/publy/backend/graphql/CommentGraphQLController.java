package nh.publy.backend.graphql;

import nh.publy.backend.domain.Comment;
import nh.publy.backend.domain.CommentCreationFailedException;
import nh.publy.backend.domain.PublyDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Controller
public class CommentGraphQLController {
  private static final Logger log = LoggerFactory.getLogger(CommentGraphQLController.class);

  @PersistenceContext
  private EntityManager entityManager;

  private final CommentEventPublisher commentEventPublisher;
  private final PublyDomainService publyDomainService;

  public CommentGraphQLController(CommentEventPublisher commentEventPublisher,
                                  PublyDomainService publyDomainService) {
    this.commentEventPublisher = commentEventPublisher;
    this.publyDomainService = publyDomainService;
  }

  @QueryMapping
  public CommentConnection comments(CommentsInput paginationInput, @Argument("condition") Optional<CommentFilterCondition> commentCondition) {
    log.info("Comment condition {}", commentCondition);
    return new CommentConnection(entityManager,
      paginationInput,
      commentCondition,
      paginationInput.getOrderBy()
    );
  }

  @MutationMapping
  public AddCommentPayload addComment(@Argument AddCommentInput input) {
    try {
      Comment comment = publyDomainService.addComment(
        input.storyId().getStoryId(),
        input.content());
      return new AddCommentSuccessPayload(comment);
    } catch (CommentCreationFailedException ex) {
      return new AddCommentFailurePayload(ex.getMessage());
    }
  }

  @SchemaMapping
  public NodeId id(Comment comment) {
    return new NodeId(comment.getId(), NodeType.comment);
  }

  @SubscriptionMapping
  public Flux<OnNewCommentEvent> onNewComment(@Argument NodeId storyId) {
    log.info("Subscription for 'newComment' (" + storyId + ") received");
    return this.commentEventPublisher.
      getPublisher(storyId.getStoryId());
  }
}
