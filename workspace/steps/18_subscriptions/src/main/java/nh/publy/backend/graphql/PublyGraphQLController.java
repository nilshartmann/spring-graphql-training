package nh.publy.backend.graphql;


import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Controller
public class PublyGraphQLController {
  private final StoryRepository storyRepository;
  private final UserService userService;
  private final PublyDomainService publyDomainService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService, PublyDomainService publyDomainService) {
    this.storyRepository = storyRepository;
    this.userService = userService;
    this.publyDomainService = publyDomainService;
  }

  @QueryMapping
  public List<Story> stories() {
    return storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return storyRepository.findById(storyId);
  }

  @SchemaMapping
  public Mono<User> user(Member member) {
    return userService.findUser(member.getUserId());
  }

  static class AddCommentInput {
    private final Long storyId;
    private final Long memberId;
    private final String content;

    public AddCommentInput(Long storyId, Long memberId, String content) {
      this.storyId = storyId;
      this.memberId = memberId;
      this.content = content;
    }

    public Long getStoryId() {
      return storyId;
    }

    public Long getMemberId() {
      return memberId;
    }

    public String getContent() {
      return content;
    }
  }

  static class AddCommentPayload {
    private final Comment newComment;

    public AddCommentPayload(Comment newComment) {
      this.newComment = newComment;
    }

    public Comment getNewComment() {
      return newComment;
    }
  }

  @MutationMapping
  public AddCommentPayload addComment(@Argument AddCommentInput input) {
    Comment newComment = publyDomainService.addComment(
      input.getStoryId(),
      input.getMemberId(),
      input.getContent()
    );

    return new AddCommentPayload(newComment);
  }

  @SubscriptionMapping
  public Publisher<OnNewCommentEvent> onNewComment(@Argument Long storyId) {
    return this.publyDomainService.getOnNewCommentEventPublisher()
      .filter(e -> e.getStoryId().equals(storyId));
  }
}
