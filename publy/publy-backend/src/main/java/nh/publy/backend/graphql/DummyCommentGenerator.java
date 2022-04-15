package nh.publy.backend.graphql;

import nh.publy.backend.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static java.lang.String.*;

@RestController
public class DummyCommentGenerator {

  private static final Logger log = LoggerFactory.getLogger(DummyCommentGenerator.class);

  private final boolean autoGenerateComments;
  private final PublyDomainService publyDomainService;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private long count;

  private Member member;

  public DummyCommentGenerator(@Value("${publy.dummyContentGenerator.enable-auto}") boolean autoGenerateComments, PublyDomainService publyDomainService, MemberRepository memberRepository, CommentRepository commentRepository) {
    this.autoGenerateComments = autoGenerateComments;
    this.publyDomainService = publyDomainService;
    this.memberRepository = memberRepository;
    this.commentRepository = commentRepository;
  }

  /**
   * Auto generated dummy comments for Story 100
   */
  @Scheduled(fixedRateString = "${publy.dummyContentGenerator.addCommentRate}")
  public void autoGenerateComment() {
    if (!autoGenerateComments) {
      return;
    }

    addComment();
  }

  /**
   * Can be used to quickly generate a new dummy comment for Story with id 100
   */
  @GetMapping("/c")
  public ResponseEntity<?> c() {
    var c = addComment();
    return ResponseEntity.ok(format("Comment-Id '%s' Content: '%s'", c.getId(), c.getContent()));
  }

  private Comment addComment() {
    String content = "Comment #" + ++count;
    log.info("add comment... '{}' ", content);
    return publyDomainService.addComment(100L, this.member, content);
  }


  @PostConstruct
  public void loadMember() {
    if (autoGenerateComments) {
      log.info("Loading Member 1");
      this.member = memberRepository.findById(1L).orElseThrow();
      this.count = commentRepository.countCommentsForStory(100L);
    }
  }
}
