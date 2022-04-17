package nh.publy.backend.graphql;

import nh.publy.backend.domain.PublyDomainService;
import nh.publy.backend.domain.TopTags;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TagGraphQLController {
  private final PublyDomainService publyDomainService;

  public TagGraphQLController(PublyDomainService publyDomainService) {
    this.publyDomainService = publyDomainService;
  }

  @QueryMapping
  public List<TopTags> topTags() {
    List<TopTags> topTags = publyDomainService.getTopTags();
    return topTags;
  }
}
