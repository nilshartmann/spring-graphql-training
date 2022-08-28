package nh.publy.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.publy.domain.Story;
import nh.publy.domain.StoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryStoriesDataFetcher implements DataFetcher<List<Story>> {

  private final StoryRepository storyRepository;

  public QueryStoriesDataFetcher(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @Override
  public List<Story> get(DataFetchingEnvironment environment) throws Exception {
    return storyRepository.findAllStories();
  }
}
