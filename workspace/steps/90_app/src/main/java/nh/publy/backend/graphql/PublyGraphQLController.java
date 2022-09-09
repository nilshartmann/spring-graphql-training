package nh.publy.backend.graphql;

import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.dataloader.DataLoader;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
public class PublyGraphQLController {
  private static final Logger log = LoggerFactory.getLogger(PublyGraphQLController.class);
  private final StoryRepository storyRepository;
  private final UserService userService;
  private final PublyDomainService publyDomainService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService, PublyDomainService publyDomainService, BatchLoaderRegistry batchLoaderRegistry) {
    this.storyRepository = storyRepository;
    this.userService = userService;
    this.publyDomainService = publyDomainService;

    batchLoaderRegistry.forTypePair(String.class, User.class).registerBatchLoader((keys, env) -> {
      log.info("Loading Users with keys {}", keys);
      return userService.findUsers(keys);
    });
  }

  static class StoryConnection {
    private final PageResult page;
    private final List<Story> stories;

    public StoryConnection(PageResult page, List<Story> stories) {
      this.page = page;
      this.stories = stories;
    }

    public PageResult getPage() {
      return page;
    }

    public List<Story> getStories() {
      return stories;
    }
  }

  @QueryMapping
  public StoryConnection stories(@Argument int page, @Argument int pageSize) {
    log.info("Fetching {} stories, starting with page {}", pageSize, page);
    Pageable p = PageRequest.of(
      page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")
    );

    Page<Story> result = storyRepository.findAll(p);

    return new StoryConnection(
      PageResult.fromPage(result),
      result.getContent()
    );
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return storyRepository.findById(storyId);
  }

  @SchemaMapping
  public CompletableFuture<User> user(Member member, DataLoader<String, User> userDataLoader) {
    String userId = member.getUserId();
    log.info("Delegating load with user id {} to DataLoader", userId);
    return userDataLoader.load(userId);
  }

  static class AddCommentInput {
    private final Long storyId;
    private final String content;

    public AddCommentInput(Long storyId, String content) {
      this.storyId = storyId;
      this.content = content;
    }

    public Long getStoryId() {
      return storyId;
    }

    public String getContent() {
      return content;
    }
  }

  interface AddCommentPayload {
  }

  static class AddCommentSuccessPayload implements AddCommentPayload{
    private final Comment newComment;

    public AddCommentSuccessPayload(Comment newComment) {
      this.newComment = newComment;
    }

    public Comment getNewComment() {
      return newComment;
    }
  }

  static class AddCommentFailedPayload implements AddCommentPayload {
    private final String errorMsg;


    AddCommentFailedPayload(String errorMsg) {
      this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }

  @MutationMapping
  @PreAuthorize("hasRole('USER')")
  public AddCommentPayload addComment(@Argument AddCommentInput input, @AuthenticationPrincipal User user) {

    Long memberId = publyDomainService.getMemberForUser(user);

    try {
      Comment newComment = publyDomainService.addComment(
        input.getStoryId(),
        memberId,
        input.getContent()
      );

      return new AddCommentSuccessPayload(newComment);
    } catch (Exception ex) {
      log.error("Add Comment failed: {}", ex.getMessage(), ex);
      return new AddCommentFailedPayload(ex.getMessage());
    }
  }

  @SubscriptionMapping
  public Publisher<OnNewCommentEvent> onNewComment(@Argument Long storyId) {
    return this.publyDomainService.getOnNewCommentEventPublisher()
      .filter(e -> e.getStoryId().equals(storyId));
  }
}