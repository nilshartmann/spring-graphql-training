package nh.publy.backend.graphql;

import nh.publy.backend.domain.Story;
import nh.publy.backend.domain.StoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PublyGraphQLController {
  private final StoryRepository storyRepository;

  public PublyGraphQLController(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @QueryMapping
  public List<Story> stories() {
    return storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return storyRepository.findById(storyId);
  }
}
