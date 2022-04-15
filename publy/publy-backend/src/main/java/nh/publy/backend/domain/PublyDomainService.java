package nh.publy.backend.domain;

import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import nh.publy.backend.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static nh.publy.backend.graphql.DemoConfig.slowDownReactionSummary;

@Service
public class PublyDomainService {

  private static final Logger log = LoggerFactory.getLogger(PublyDomainService.class);

  private final ApplicationEventPublisher applicationEventPublisher;
  private final AuthenticationService authenticationService;
  private final UserService userService;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final StoryRepository storyRepository;
  private final ReactionRepository reactionRepository;

  public PublyDomainService(ApplicationEventPublisher applicationEventPublisher, AuthenticationService authenticationService, UserService userService, MemberRepository memberRepository, CommentRepository commentRepository, StoryRepository storyRepository, ReactionRepository reactionRepository) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.authenticationService = authenticationService;
    this.userService = userService;
    this.memberRepository = memberRepository;
    this.commentRepository = commentRepository;
    this.storyRepository = storyRepository;
    this.reactionRepository = reactionRepository;
  }

  public Optional<Member> getCurrentMember() {
    return userService.getCurrentUser()
      .flatMap(user -> memberRepository.findByUserId(user.getId()));
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
  @Transactional
  public Story toggleReaction(
    @NotNull Long storyId,
    @NotNull ReactionType reactionType
  ) {
    Member member = getCurrentMember().orElseThrow();
    Story story = storyRepository.findById(storyId).orElseThrow();

    if (!story.removeReactionByUser(member, reactionType)) {
      Long reactionId = reactionRepository.getNextReactionId();
      log.info("Reaction Type not given by member {} adding to story {} ({}) with new id {}", member.getId(), story.getId(), story.getVersion(), reactionId);

      story.addReaction(reactionId, member, reactionType);
    } else {
      log.info("Reaction Type already given by member {} to story {} ({}), reaction count: {}", member.getId(), story.getId(), story.getVersion(), story.getReactions().size());
    }
    storyRepository.save(story);
    return story;
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
  @Transactional
  public Comment addComment(
    @NotNull Long storyId,
    @NotNull String content) {

    // EXAMPLE: how to deal with bussiness errors
    if (content.length() < 5) {
      throw new CommentCreationFailedException("Please enter at least five chars!");
    }

    Member member = getCurrentMember().orElseThrow();

    return addComment(storyId, member, content);
  }

  @Transactional
  public Comment addComment(
    @NotNull Long storyId,
    @NotNull Member member,
    @NotNull String content) {

    Story story = storyRepository.findById(storyId).orElseThrow();

    Long newId = commentRepository.getNextCommentId();

    Comment comment = new Comment(newId, story, member, content);
    comment = commentRepository.save(comment);

    applicationEventPublisher.publishEvent(new CommentAddedEvent(comment, storyId, newId));

    return comment;
  }

  /**
   * Returns the topTags for a Member
   * <p>
   * Top tags are calculated with a high complex totaly secret algorithm that is very important
   * for the success of our business!
   * <p>
   * If no member is logged in, all tags are considered, otherwise only those, the current user
   * already has written stories for
   */
  public List<TopTags> getTopTags() {
    List<String> tags = getCurrentMember().map(member -> storyRepository.findTopTagsFor(member.getId()))
      .orElseGet(storyRepository::findTopTags);

    List<TopTags> result = tags.stream().map(tagName -> {
      var stories = storyRepository.getNewestStoriesWithTag(tagName);
      return new TopTags(tagName, stories);
    }).toList();

    return result;
  }

  @Async
  public CompletableFuture<ReactionSummary> getReactionSummary(Long storyId, Optional<ReactionType> limitToReactionType) {
    log.info("Getting Reaction Summary for Story {}", storyId);

    // simulate even slower processing time
    slowDownReactionSummary();

    Optional<User> currentUser = userService.getCurrentUser();
    List<ReactionType> typesByMe = currentUser
      .flatMap(user -> memberRepository.findByUserId(user.getId()))
      .map(member -> reactionRepository.reactionsForStoryAndUser(storyId, member.getId()))
      .orElse(Collections.emptyList());

    List<ReactionByType> reactionsByType = reactionRepository.countByReactionType(storyId)
      .stream()
      .map(count -> new ReactionByType(count.getType(), count.getTotalCount(), typesByMe.contains(count.getType())))
      .toList();

    var result = Arrays.stream(ReactionType.values())
      .filter(rt -> limitToReactionType.isEmpty() || limitToReactionType.get() == rt)
      .map(type -> reactionsByType.stream().filter(y -> y.type() == type).findAny()
        .orElse(new ReactionByType(type, BigInteger.ZERO, false)
        )).toList();

    return CompletableFuture.completedFuture(new ReactionSummary(result));
  }

  /**
   *
   */
  @PreAuthorize("hasRole('ROLE_USER')")
  @Transactional
  public Story addStory(
    @Size(min = 10)
      String title,
    @Size(min = 1) String[] tags,
    @Size(min = 10)
      String body) {
    Long storyId = storyRepository.getNextStoryId();
    Member member = getCurrentMember().orElseThrow();
    Story story = new Story(storyId, member, title, tags, body);
    return storyRepository.save(story);
  }
}
