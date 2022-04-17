package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.Story;
import nh.publy.backend.domain.StoryRepository;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Controller
public class PublyGraphQLController {
  private final StoryRepository storyRepository;
  private final UserService userService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService) {
    this.storyRepository = storyRepository;
    this.userService = userService;
  }

  @QueryMapping
  public List<Story> stories() {
    return storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return storyRepository.findById(storyId);
  }

  @SchemaMapping
  public Mono<User> user(Member member) {
    return userService.findUser(member.getUserId());
  }
}
