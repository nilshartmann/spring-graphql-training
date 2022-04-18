package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.Story;
import nh.publy.backend.domain.StoryRepository;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class PublyGraphQLController {
  private static final Logger log = LoggerFactory.getLogger( PublyGraphQLController.class );
  private final StoryRepository storyRepository;
  private final UserService userService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService) {
    this.storyRepository = storyRepository;
    this.userService = userService;

//    batchLoaderRegistry.forTypePair(String.class, User.class).registerBatchLoader( (keys, env) -> {
//      log.info("Loading Users with keys {}", keys);
//      return userService.findUsers(keys);
//    });
  }

  @QueryMapping
  public List<Story> stories() {
    return storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return storyRepository.findById(storyId);
  }

//  @SchemaMapping
//  public CompletableFuture<User> user(Member member, DataLoader<String, User> userDataLoader) {
//    String userId = member.getUserId();
//    log.info("Delegating load with user id {} to DataLoader", userId);
//    return userDataLoader.load(userId);
//  }

  @BatchMapping
  public Flux<User> user(List<Member> members) {
    List<String> keys = members.stream().map(Member::getUserId).collect(Collectors.toList());

    log.info("Loading Users with ids {} in BatchMapping", keys);
    return userService.findUsers(keys);
  }

//  @SchemaMapping
//  CompletableFuture<User> user(Member member, DataLoader<String, User> dataLoader) {
//    return dataLoader.load(member.getUserId());
//  }

  static class AddCommentInput {
    private final Long storyId;
    private final Long memberId;
    private final @Size(min=5) String content;

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

  @Autowired MemberRepository memberRepository;

  @MutationMapping
  public Comment addComment(@Valid @Argument AddCommentInput input,
                            @AuthenticationPrincipal User user) {
    Long memberIdForUserId = memberRepository.getMemberIdForUserId(user.getId());
    return publyDomainService.addComment(
      input.getStoryId(),
      memberIdForUserId,
      input.getContent()
    );
  }

}
