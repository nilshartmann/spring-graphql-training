package nh.publy.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.publy.domain.Story;
import org.springframework.stereotype.Component;

@Component
public class ExcerptDataFetcher implements DataFetcher<String> {
  @Override
  public String get(DataFetchingEnvironment environment) throws Exception {
    Story story = environment.getSource();
    String excerpt = story.getBody().substring(0, 3);
    return excerpt;
  }
}
