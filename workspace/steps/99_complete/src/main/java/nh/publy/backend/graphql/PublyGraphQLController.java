package nh.publy.backend.graphql;

import graphql.schema.DataFetchingFieldSelectionSet;
import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.Story;
import nh.publy.backend.domain.StoryRepository;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import nh.publy.backend.util.DemoConfig;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class PublyGraphQLController {

  private static final Logger log = LoggerFactory.getLogger(PublyGraphQLController.class);

  private final StoryRepository storyRepository;
  private final UserService userService;

  public PublyGraphQLController(StoryRepository storyRepository, UserService userService, BatchLoaderRegistry registry) {
    this.storyRepository = storyRepository;
    this.userService = userService;
//    registry.forTypePair(String.class, User.class).registerBatchLoader(
//      (List<String> keys, BatchLoaderEnvironment env) -> {
//        log.info("Loading Users with keys {}", keys);
//        return userService.findUsers(keys);
//      }
//    );
  }

  @QueryMapping
  public List<Story> stories() {
    return this.storyRepository.findAll();
  }

  @QueryMapping
  public Optional<Story> story(@Argument Long storyId) {
    return this.storyRepository.findById(storyId);
  }

  @SchemaMapping
  CompletableFuture<String> excerpt(Story story, @Argument int maxLength) {
    return CompletableFuture.supplyAsync(() -> {
      DemoConfig.slowDown("Reading Excerpt for story " + story.getId());
      return story.getBody().substring(0, maxLength);

    });
  }

  @BatchMapping
  Flux<User> user(List<Member> member) {
    List<String> keys = member.stream().map(Member::getUserId).collect(Collectors.toList());
    log.info("Loading Users with BatchMapping for keys {}", keys);
    return userService.findUsers(keys);
  }

//  @SchemaMapping
//  CompletableFuture<User> user(Member member, DataLoader<String, User> dataLoader) {
//    return dataLoader.load(member.getUserId());
//  }

}
