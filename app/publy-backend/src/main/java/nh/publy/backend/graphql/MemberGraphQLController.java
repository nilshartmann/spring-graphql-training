package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.MemberRepository;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static java.lang.String.*;

@Controller
public class MemberGraphQLController {

  @PersistenceContext
  private EntityManager entityManager;

  private final MemberRepository memberRepository;
  private final UserService userService;

  private final String profileImageBaseUrl;

  private static final Logger log = LoggerFactory.getLogger(MemberGraphQLController.class);

  public MemberGraphQLController(MemberRepository memberRepository, UserService userService, @Value("${publy.profileImageBaseUrl}") String profileImageBaseUrl,
                                 BatchLoaderRegistry batchLoaderRegistry) {
    this.memberRepository = memberRepository;
    this.userService = userService;
    this.profileImageBaseUrl = profileImageBaseUrl;

//    batchLoaderRegistry.forTypePair(String.class, User.class).registerBatchLoader(
//      (List<String> userIds, BatchLoaderEnvironment env) -> {
//        log.info("Loading User with ids {}", userIds);
//        return Flux.fromStream(userIds.stream()).flatMap(userService::findUser);
//      });
  }

  @QueryMapping
  public Optional<Member> me() {
    Optional<Member> member = userService.getCurrentUser()
      .flatMap(u -> memberRepository.findByUserId(u.getId()));
    return member;
  }

//  @QueryMapping
//  public Optional<Member> member(@Argument Long id) {
//    return memberRepository.findById(id);
//  }

  @QueryMapping
  public Optional<Member> member(@Argument NodeId id) {
    return memberRepository.findById(id.getMemberId());
  }

  @QueryMapping
  public Page<Member> members(@Argument Optional<Integer> page, @Argument Optional<Integer> size) {
    Pageable p = PageRequest.of(
      page.map(pp -> pp > 0 ? pp - 1 : pp).orElse(0),
      size.orElse(10)
    );
    var members = memberRepository.findAll(p);
    return members;
  }

  @SchemaMapping
  public CommentConnection comments(Member member, CommentsInput input) {
    return new CommentConnection(
      entityManager,
      input,
      Optional.of(new CommentFilterCondition(CommentConditionField.writtenBy, member.getId())),
      input.getOrderBy()
    );
  }

  @SchemaMapping
  public NodeId id(Member member) {
    return new NodeId(member.getId(), NodeType.member);
  }

  @SchemaMapping
  public String profileImageUrl(Member member) {
    return format("%s%s", profileImageBaseUrl, member.getProfileImage());
  }

  @SchemaMapping
  public StoryConnection stories(Member member, StoriesInput input) {
    return new StoryConnection(
      entityManager,
      input,
      Optional.of(StoryFilterCondition.writtenByMember(member)),
      input.getOrderBy());
  }

  // ------------------------- EIGENE STÄNDE ---------------------------
//  @BatchMapping
//  public Flux<User> user(List<Member> members) {
//    log.info("Collecting Users for {}", members);
//    return userService.findUsers(
//      members.stream().map(Member::getUserId).toList()
//    );
//  }

//  @SchemaMapping
//  public CompletableFuture<User> user(Member member, DataLoader<String, User> dataLoader) {
//    String userId = member.getUserId();
//    return dataLoader.load(userId);
//  }

//  @SchemaMapping
//  public Mono<User> user(Member member) {
//    String userId = member.getUserId();
//    return userService.findUser(userId);
//  }

  @SchemaMapping
  public User user(Member member) {
    String userId = member.getUserId();
    return userService.findUserSync(userId);
  }

}
