package nh.publy.backend.domain;

import io.reactivex.rxjava3.core.Flowable;
import nh.publy.backend.domain.user.UserService;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Service
public class PublyDomainService {

  private static final Logger log = LoggerFactory.getLogger(PublyDomainService.class);

  private final ApplicationEventPublisher applicationEventPublisher;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final StoryRepository storyRepository;
  private final ReactionRepository reactionRepository;
  private final UserService userService;
  private final Sinks.Many<OnNewCommentEvent> sink;


  public PublyDomainService(ApplicationEventPublisher applicationEventPublisher, MemberRepository memberRepository, CommentRepository commentRepository, StoryRepository storyRepository, ReactionRepository reactionRepository, UserService userService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.memberRepository = memberRepository;
    this.commentRepository = commentRepository;
    this.storyRepository = storyRepository;
    this.reactionRepository = reactionRepository;
    this.userService = userService;

    this.sink = Sinks.many()
      .multicast()
      .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
  }

  @Transactional
  public Story toggleReaction(
    @NotNull Long storyId,
    @NotNull Long memberId,
    @NotNull ReactionType reactionType
  ) {
    Member member = memberRepository.findById(memberId).orElseThrow();
    Story story = storyRepository.findById(storyId).orElseThrow();

    if (!story.removeReactionByUser(member, reactionType)) {
      Long reactionId = reactionRepository.getNextReactionId();
      log.info("Reaction Type not given by member {} adding to story {} ({}) with new id {}", member.getId(), story.getId(), story.getVersion(), reactionId);

      story.addReaction(reactionId, member, reactionType);
      applicationEventPublisher.publishEvent(new ReactionAddedEvent(story.getId(), reactionId));
    } else {
      log.info("Reaction Type already given by member {} to story {} ({}), reaction count: {}", member.getId(), story.getId(), story.getVersion(), story.getReactions().size());
    }
    storyRepository.save(story);
    return story;
  }

  @Transactional
  public Comment addComment(
    @NotNull Long storyId,
    @NotNull Long memberId,
    @NotNull String content) {

    // EXAMPLE: how to deal with bussiness errors
    if (content.length() < 5) {
      throw new CommentCreationFailedException("Please enter at least five chars!");
    }

    Member member = memberRepository.findById(memberId).orElseThrow();

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

    applicationEventPublisher.publishEvent(new CommentAddedEvent(story.getId(), newId));

    return comment;
  }

  /**
   *
   */
  @Transactional
  public Story addStory(
    @NotNull Long memberId,
    @Size(min = 10) String title,
    @Size(min = 10) String body) {
    Long storyId = storyRepository.getNextStoryId();
    Member member = memberRepository.findById(memberId).orElseThrow();
    Story story = new Story(storyId, member, title, body);
    return storyRepository.save(story);
  }

  @Transactional
  @PreAuthorize("hasRole('ROLE_EDITOR')")
  public Story addStory(@Size(min = 10) String title,
                        @Size(min = 10) String body) {
    Member member = getCurrentMember();

    Long storyId = storyRepository.getNextStoryId();
    Story story = new Story(storyId, member, title, body);

    return storyRepository.save(story);
  }

  public Member getCurrentMember() {
    return userService.getCurrentUser()
      .map(user -> memberRepository.getByUserId(user.getId()))
      .orElseThrow();
  }

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void onNewComment(CommentAddedEvent event) {
    log.trace("onNewComment received event for comment {} and story {}, currentSubscriberCount {}",
      event.getCommentId(),
      event.getStoryId(),
      this.sink.currentSubscriberCount()
    );

    this.sink.emitNext(new OnNewCommentEvent(
      commentRepository,
      event.getStoryId(),
      event.getCommentId()
    ), Sinks.EmitFailureHandler.FAIL_FAST);
  }

  public Flowable<OnNewCommentEvent> getOnNewCommentEventPublisher() {
    return Flowable.fromPublisher(this.sink.asFlux());
  }

}
