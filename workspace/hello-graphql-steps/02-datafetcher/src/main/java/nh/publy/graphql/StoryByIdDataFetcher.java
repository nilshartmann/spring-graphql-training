package nh.publy.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.publy.domain.Story;
import nh.publy.domain.StoryRepository;

import java.util.Optional;

public class StoryByIdDataFetcher implements DataFetcher<Optional<Story>> {

  private final StoryRepository storyRepository;

  public StoryByIdDataFetcher(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @Override
  public Optional<Story> get(DataFetchingEnvironment environment) throws Exception {
    String storyId = environment.getArgument("storyId");
    Optional<Story> story = storyRepository.findStoryById(Long.parseLong(storyId));
    return story;
  }
}
