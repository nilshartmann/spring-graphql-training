package nh.publy.backend.graphql;

import nh.publy.backend.domain.CommentRepository;
import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.MemberRepository;
import nh.publy.backend.domain.PublyDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Can be used to automatically insert new comments (simulating user interactions)
 */
@Component
public class DummyCommentGenerator {

  private static final Logger log = LoggerFactory.getLogger(DummyCommentGenerator.class);

  private final boolean autoGenerateComments;
  private final PublyDomainService publyDomainService;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private long count;

  private Member member;

  public DummyCommentGenerator(@Value("${publy.dummyContentGenerator.enable-auto:false}") boolean autoGenerateComments, PublyDomainService publyDomainService, MemberRepository memberRepository, CommentRepository commentRepository) {
    this.autoGenerateComments = autoGenerateComments;
    this.publyDomainService = publyDomainService;
    this.memberRepository = memberRepository;
    this.commentRepository = commentRepository;
  }

  /**
   * Auto generated dummy comments for Story 100
   */
  @Scheduled(fixedRateString = "${publy.dummyContentGenerator.addCommentRate:2000}")
  public void autoGenerateComment() {
    if (!autoGenerateComments) {
      return;
    }

    String content = "Comment #" + ++count;
    publyDomainService.addComment(100L, this.member, content);
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
