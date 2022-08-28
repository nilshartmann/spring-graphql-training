package nh.publy;

import graphql.ExecutionResult;
import nh.publy.graphql.GraphQLProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

public class QueryTest {

  GraphQLProvider graphQLProvider = new GraphQLProvider();

  @Test
  void storiesQueryWorks() {

    ExecutionResult executionResult = graphQLProvider.
      getGraphQL()
      .execute("query { stories { id title excerpt writtenBy { id } } }");

    assertThat(executionResult.getErrors())
      .isEmpty();

    Object data = executionResult.getData();
    assertThat(data).isInstanceOf(Map.class);
    Map<?, ?> map = (Map<?, ?>) data;
    assertThat(map.containsKey("stories")).isTrue();
    List<?> stories = (List<?>)map.get("stories");
    assertThat(stories).hasSize(5);

    assertStory((Map<?, ?>)stories.get(0), "1", "Bod", "1");
    assertStory((Map<?, ?>)stories.get(2), "3", "Bod", "3");
    assertStory((Map<?, ?>)stories.get(4), "5", "Bod", "2");
  }

  @Test
  void storyByIdQueryWorks() {

    ExecutionResult executionResult = graphQLProvider.
      getGraphQL()
      .execute("query { story(storyId: 2) { id title excerpt writtenBy { id } } }");

    assertThat(executionResult.getErrors())
      .isEmpty();

    Object data = executionResult.getData();
    assertThat(data).isInstanceOf(Map.class);
    Map<?, ?> map = (Map<?, ?>) data;
    assertThat(map).hasSize(1);
    assertThat(map.containsKey("story")).isTrue();
    Map<?, ?> story = (Map<?, ?>)map.get("story");


    assertStory(story, "2", "Bod", "2");
  }

  private void assertStory(Map<?, ?> story, String id, String excerpt, String writtenById) {
    assertThat(story.get("id")).isEqualTo(id);
    assertThat(story.get("excerpt")).isEqualTo(excerpt);
    assertThat(story.get("writtenBy")).isNotNull();
    Map<?, ?> writtenByResult = (Map<?, ?>)story.get("writtenBy");
    assertThat(writtenByResult.get("id")).isEqualTo(writtenById);
  }

}
