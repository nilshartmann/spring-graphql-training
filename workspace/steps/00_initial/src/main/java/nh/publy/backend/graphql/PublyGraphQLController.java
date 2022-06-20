package nh.publy.backend.graphql;

import nh.publy.backend.domain.StoryRepository;
import nh.publy.backend.domain.user.UserService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PublyGraphQLController {

  private final StoryRepository storyRepository;
  private final UserService userService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService) {
    this.storyRepository = storyRepository;
    this.userService = userService;
  }

  @QueryMapping
  public String ping() {
    return "Pong!";
  }
}
