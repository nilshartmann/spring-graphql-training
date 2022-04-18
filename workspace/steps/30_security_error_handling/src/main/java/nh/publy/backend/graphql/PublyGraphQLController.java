package nh.publy.backend.graphql;

import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import nh.publy.backend.util.DemoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class PublyGraphQLController {

  private static final Logger log = LoggerFactory.getLogger(PublyGraphQLController.class);

  private final StoryRepository storyRepository;
  private final UserService userService;
  private final PublyDomainService publyDomainService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService, BatchLoaderRegistry registry, PublyDomainService publyDomainService) {
    this.storyRepository = storyRepository;
    this.userService = userService;
//    registry.forTypePair(String.class, User.class).registerBatchLoader(
//      (List<String> keys, BatchLoaderEnvironment env) -> {
//        log.info("Loading Users with keys {}", keys);
//        return userService.findUsers(keys);
//      }
//    );
    this.publyDomainService = publyDomainService;
  }

  @QueryMapping
  public List<Story> stories() {
    return this.storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return this.storyRepository.findById(storyId);
  }

  @SchemaMapping
  CompletableFuture<String> excerpt(Story story, @Argument int maxLength) {
    return CompletableFuture.supplyAsync(() -> {
      DemoConfig.slowDown("Reading Excerpt for story " + story.getId());
      return story.getBody().substring(0, maxLength);

    });
  }

  @BatchMapping
  Flux<User> user(List<Member> member) {
    List<String> keys = member.stream().map(Member::getUserId).collect(Collectors.toList());
    log.info("Loading Users with BatchMapping for keys {}", keys);
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
  public Comment addComment(@Argument AddCommentInput input,
                            @AuthenticationPrincipal User user) {
    Long memberIdForUserId = memberRepository.getMemberIdForUserId(user.getId());
    return publyDomainService.addComment(
      input.getStoryId(),
      memberIdForUserId,
      input.getContent()
    );
  }

}
