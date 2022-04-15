package nh.publy.backend.graphql;

import graphql.schema.DataFetchingFieldSelectionSet;
import nh.publy.backend.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Controller
public class StoryGraphQLController {

  private static final Logger log = LoggerFactory.getLogger(StoryGraphQLController.class);

  @PersistenceContext
  private EntityManager entityManager;

  private final MarkdownService markdownService;
  private final PublyDomainService publyDomainService;

  private final StoryRepository storyRepository;

  public StoryGraphQLController(MarkdownService markdownService, PublyDomainService publyDomainService, StoryRepository storyRepository) {
    this.markdownService = markdownService;
    this.publyDomainService = publyDomainService;
    this.storyRepository = storyRepository;
  }

  // 1. Iteration: Nur story zurückgeben (ohne id)
  // 2. Iteration: Argument id
  // 3. Iteration DataFetchingFieldSelectionSet
  @QueryMapping
  public Optional<Story> story(@Argument NodeId id, DataFetchingFieldSelectionSet fieldSelectionSet) {
    fieldSelectionSet.getFields()
      .forEach(s -> log.info("Selected field: '{}'", s));

    boolean withWrittenBy = fieldSelectionSet.contains("Story.writtenBy");

    if (id == null) {
      if (withWrittenBy) {
        return storyRepository.findFirstWithAuthorByOrderByCreatedAtDesc();
      }
      return storyRepository.findFirstByOrderByCreatedAtDesc();
    }
    Long nodeId = id.getStoryId();
    if (withWrittenBy) {
      return storyRepository.findStoryAndAuthorById(nodeId);
    }

    return storyRepository.findById(nodeId);
  }

  @QueryMapping
  public StoryConnection stories(StoriesInput input, @Argument("condition") Optional<StoryFilterCondition> condition) {
    return new StoryConnection(
      entityManager,
      input,
      condition,
      input.getOrderBy()
    );
  }

  @MutationMapping
  public Object addStory(@Argument AddStoryInput input) {
    try {
      Story newStory = publyDomainService.addStory(
        input.title(),
        input.tags().toArray(new String[0]),
        input.body()
      );

      return new AddStorySuccessPayload(newStory);

    } catch (AuthenticationException | AccessDeniedException ae) {
      Optional<Member> currentMember = publyDomainService.getCurrentMember();
      return new AddStoryInvalidCredentialsPayload(ae.getMessage(), currentMember);
    } catch (RuntimeException ex) {
      Throwable root = NestedExceptionUtils.getRootCause(ex);
      if (root instanceof ConstraintViolationException cve) {
        Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
        return new AddStoryConstraintViolationPayload(constraintViolations);
      }
      throw ex;
    }
  }

  @MutationMapping
  public ToggleReactionPayload toggleReaction(@Argument ToggleReactionInput input) {
    Story updatedStory = publyDomainService.toggleReaction(
      input.storyId().getStoryId(),
      input.reactionType()
    );
    return new ToggleReactionPayload(updatedStory);
  }

  @SchemaMapping
  public String body(Story story) {
    return story.getBodyMarkdown();
  }

  @SchemaMapping
  public CommentConnection comments(Story story, CommentsInput input) throws InterruptedException {
    return new CommentConnection(entityManager,
      input,
      Optional.of(new CommentFilterCondition(CommentConditionField.story, story.getId())),
      input.getOrderBy()
    );
  }

  /**
   * This field is marked as deprecated in our GraphQL schema
   */
  @Deprecated
  @SchemaMapping
  public CommentConnection allComments(Story story, CommentsInput input) throws InterruptedException {
    return new CommentConnection(entityManager,
      input,
      Optional.of(new CommentFilterCondition(CommentConditionField.story, story.getId())),
      input.getOrderBy()
    );
  }

  @SchemaMapping
  public String excerpt(Story story, @Argument int maxLength) {
    String plainBody = markdownService.toPlainText(story.getBodyMarkdown());
    String excerpt = plainBody.length() > maxLength ? plainBody.substring(0, maxLength) + "..." : plainBody;
    return excerpt;
  }

  @SchemaMapping
  public NodeId id(Story story) {
    return new NodeId(story.getId(), NodeType.story);
  }

  @SchemaMapping
  public CompletableFuture<ReactionSummary> reactionSummary(Story story, @Argument Optional<ReactionType> type) {
    return publyDomainService.getReactionSummary(story.getId(), type);
  }

  @SchemaMapping
  public ReactionConnection reactions(Story story, PaginationInput input) throws InterruptedException {
    return new ReactionConnection(entityManager, input,
      Optional.of(new ReactionFilterCondition(ReactionConditionField.story, story.getId())),
      Optional.empty()
    );
  }


}
