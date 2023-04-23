package nh.publy.backend.util;

import nh.publy.backend.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoCommentGenerator {

  private static final Logger log = LoggerFactory.getLogger(DemoCommentGenerator.class);

  private final boolean autoGenerateComments;
  private final PublyDomainService publyDomainService;

  private long count = 0;

  public DemoCommentGenerator(@Value("${publy.dummyContentGenerator.enable-auto}") boolean autoGenerateComments, PublyDomainService publyDomainService) {
    this.autoGenerateComments = autoGenerateComments;
    this.publyDomainService = publyDomainService;
  }

  /**
   * Auto generated dummy comments for Story 1
   */
  @Scheduled(fixedRateString = "${publy.dummyContentGenerator.addCommentRate}")
  public void autoGenerateComment() {
    if (!autoGenerateComments) {
      return;
    }

    String content = "Comment #" + ++count;
    log.info("add comment... '{}' ", content);
    publyDomainService.addComment(1L, 1L, content);
  }

}
