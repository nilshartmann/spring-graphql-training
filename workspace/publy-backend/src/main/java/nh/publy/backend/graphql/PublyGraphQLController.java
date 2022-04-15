package nh.publy.backend.graphql;

import graphql.schema.DataFetchingFieldSelectionSet;
import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Controller
public class PublyGraphQLController {

  private static final Logger log = LoggerFactory.getLogger(PublyGraphQLController.class);

  private StoryRepository storyRepository;
  private MarkdownService markdownService;
  private MemberRepository memberRepository;
  private PublyDomainService publyDomainService;
  private CommentRepository commentRepository;
  private CommentEventPublisher commentEventPublisher;
  private ReactionRepository reactionRepository;
  private FeedbackPublisher feedbackPublisher;
  private UserService userService;

  public PublyGraphQLController(StoryRepository storyRepository,
                                MarkdownService markdownService,
                                MemberRepository memberRepository,
                                PublyDomainService publyDomainService,
                                CommentRepository commentRepository,
                                CommentEventPublisher commentEventPublisher,
                                ReactionRepository reactionRepository,
                                FeedbackPublisher feedbackPublisher,
                                UserService userService) {
    this.storyRepository = storyRepository;
    this.markdownService = markdownService;
    this.memberRepository = memberRepository;
    this.publyDomainService = publyDomainService;
    this.commentRepository = commentRepository;
    this.commentEventPublisher = commentEventPublisher;
    this.reactionRepository = reactionRepository;
    this.feedbackPublisher = feedbackPublisher;
    this.userService = userService;
  }

  @QueryMapping("story")
  public Optional<Story> getStory(@Argument("storyId") NodeId id,
                                  DataFetchingFieldSelectionSet selectionSet) {

    log.info("SELECTED FIELDS {}", selectionSet.getFields());

    if (id != null) {
      if (selectionSet.contains("Story.writtenBy")) {
        return storyRepository.findStoryAndAuthorById(id.getStoryId());
      }
      return storyRepository.findById(id.getStoryId());
    }

    if (selectionSet.contains("Story.writtenBy")) {
      return storyRepository.findFirstWithAuthorByOrderByCreatedAtDesc();
    }

    return storyRepository.findFirstByOrderByCreatedAtDesc();
  }

  @SchemaMapping(typeName = "Story", field = "body")
  public String body(Story story) {
    return story.getBodyMarkdown();
  }

  @SchemaMapping
  public String excerpt(Story story, @Argument int maxLength) {
    String plainBody = markdownService.toPlainText(story.getBodyMarkdown());
    String excerpt = plainBody.length() > maxLength ? plainBody.substring(0, maxLength) + "..." : plainBody;
    return excerpt;
  }

  @QueryMapping
  public List<Member> members(@Valid MemberPagination memberPagination) {

    Pageable pageable = PageRequest.of(
      memberPagination.getPage().orElse(0),
      memberPagination.getSize().orElse(10)
    );

    Page<Member> result = memberRepository.findAll(pageable);
    return result.getContent();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @MutationMapping
  public Story toggleReaction(@Argument ToggleReactionInput input,
                              @AuthenticationPrincipal User user) {

    Member member = memberRepository.getByUserId(user.getId());

    return publyDomainService.toggleReaction(input.storyId().getStoryId(),
      member.getId(), input.reactionType());
  }

  @MutationMapping
  public Object addComment(@Argument NodeId storyId,
                           @Argument NodeId memberId,
                           @Argument String content) {
    try {
      Comment comment = publyDomainService.addComment(
        storyId.getStoryId(),
        memberId.getMemberId(),
        content);

      return new AddCommentSuccessResult(comment);
    } catch (Exception ex) {
      return new AddCommentFailedPayload(ex.getMessage());
    }
  }

  @SchemaMapping
  public NodeId id(Story story) {
    return new NodeId(story.getId(), NodeType.story);
  }

  @SchemaMapping
  public NodeId id(Member member) {
    return new NodeId(member.getId(), NodeType.member);
  }

  @SchemaMapping
  public NodeId id(Comment comment) {
    return new NodeId(comment.getId(), NodeType.comment);
  }

  @QueryMapping
  public Object node(@Argument NodeId id, DataFetchingFieldSelectionSet selectionSet) {
    NodeType nodeType = id.getNodeType();

    return switch (nodeType) {
      case story -> getStory(id, selectionSet);
      case member -> memberRepository.findById(id.getMemberId());
      case comment -> commentRepository.findById(id.getCommentId());
      case reaction -> reactionRepository.findById(id.getReactionId());
    };
  }

  @QueryMapping
  public StoryPage stories(@Argument StoryFilter storyFilter, @Argument Optional<Integer> page, @Argument Optional<Integer> size) {
    Pageable pageable = PageRequest.of(
      page.orElse(0),
      size.orElse(10),
      Sort.by(Sort.Direction.DESC, "createdAt")
    );

    Page<Story> result;

    if (storyFilter.field() == StoryFilterField.newerThan) {
      result = storyRepository.findAllByCreatedAtGreaterThan(
        LocalDateTime.parse(storyFilter.value(),
          DateTimeFormatter.ISO_DATE_TIME), pageable);
    } else {
      result = storyRepository.findAllByWrittenById(
        new NodeId(storyFilter.value()).getMemberId(),
        pageable);
    }
      

    return new StoryPage(
      result.getContent(),
      result.hasPrevious(),
      result.hasNext(),
      result.getTotalElements()
    );
  }

  @SchemaMapping
  public StoryPage stories(Member member, @Argument Optional<Integer> page, @Argument Optional<Integer> size) {
    var storyFilter = new StoryFilter(
      StoryFilterField.writtenBy,
      NodeId.forMember(member.getId()).toString());
    return stories(storyFilter, page, size);
  }

  @SubscriptionMapping
  public Flux<OnNewCommentEvent> onNewComment(@Argument NodeId storyId) {
    return commentEventPublisher.getNewCommentEventPublisher(storyId.getStoryId());
  }

  @SubscriptionMapping
  public Flux<FeedbackReceivedEvent> onNewFeedback(@Argument NodeId storyId) {
    return feedbackPublisher.getFeedbackPublisher(storyId);
  }

  @SchemaMapping(typeName = "OnNewCommentEvent")
  public Comment newComment(FeedbackReceivedEvent event) {
    return commentRepository.findById(event.feedbackNodeId().getCommentId()).orElseThrow();
  }

  @SchemaMapping(typeName = "OnNewReactionEvent")
  public Reaction newReaction(FeedbackReceivedEvent event) {
    return reactionRepository.findById(event.feedbackNodeId().getReactionId()).orElseThrow();
  }

  @SchemaMapping
  public CompletableFuture<ReactionSummary> reactionSummary(Story story, @Argument Optional<NodeId> forMember) {
    return publyDomainService.getReactionSummary(
      story.getId(),
      forMember.map(NodeId::getMemberId)
    );
  }

  @BatchMapping
  public Flux<User> user(List<Member> members) {
    // Alternativ:
    //  List<User>
    //  Map<Member, User>
    //  Mono<Map<Member, User>>
    List<String> userIds = members.stream().map(Member::getUserId).toList();

    log.info("Reading UserIds for members {}", userIds);

    return userService.findUsers(userIds);
  }

  @BatchMapping
  public Map<Story, List<Comment>> comments(List<Story> stories) {
    List<Long> storyIds = stories.stream().map(Story::getId).toList();

    log.info("Reading Comments for Stories with ids {}", storyIds);

    List<Comment> allCommentsForStories = commentRepository.findCommentsForStories(storyIds);
    Map<Story, List<Comment>> result = new HashMap<>();
    stories.forEach(s -> result.put(s, new LinkedList<>()));

    allCommentsForStories.forEach(comment -> {
      result.get(comment.getStory()).add(comment);
    });

    return result;
  }

  @MutationMapping
  public Object addStory(@Argument AddStoryInput input,
                         @AuthenticationPrincipal User user) {

    try {
      Story newStory = publyDomainService.addStory(
        input.title(),
        input.tags().toArray(new String[0]),
        input.body()
      );

      return new AddStorySuccessPayload(newStory);
    } catch (AuthenticationException | AccessDeniedException ex) {
      Member member = user != null ? memberRepository.getByUserId(user.getId()) : null;
      return new AddStoryInvalidCredentialsPayload(ex.getMessage(), member);
    } catch (RuntimeException ex) {
      Throwable throwable = NestedExceptionUtils.getRootCause(ex);
      if (throwable instanceof ConstraintViolationException cve) {
        List<AddStoryConstraintViolation> addStoryConstraintViolations = cve.getConstraintViolations().stream()
          .map(violation -> new AddStoryConstraintViolation(
            violation.getMessage(),
            violation.getPropertyPath().toString()
          )).toList();

        return new AddStoryConstraintViolationPayload(addStoryConstraintViolations);


      }

      return new AddStoryFailedPayload(ex.getMessage());


    } catch (Exception ex) {
      return new AddStoryFailedPayload(ex.getMessage());
    }
  }

  @QueryMapping
  public Optional<Member> member(@Argument NodeId id) {
    return memberRepository.findById(id.getMemberId());
  }
}

