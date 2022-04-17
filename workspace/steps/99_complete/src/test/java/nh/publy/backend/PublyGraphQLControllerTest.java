package nh.publy.backend;


import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.UserService;
import nh.publy.backend.graphql.PublyGraphQLController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@GraphQlTest
public class PublyGraphQLControllerTest {
  @MockBean StoryRepository storyRepository;
  @MockBean UserService userService;

  @Autowired
  GraphQlTester graphQlTester;

   // language=GraphQL
  private final String query = "query ($storyId: ID!) {\n" +
    "    story(storyId: $storyId) {\n" +
    "      id title\n" +
    "    }\n" +
    "  }";

  private final Story story = new Story(1L,
    null, "Hurray!", new String[]{"graphql"}, "Great Story!");

  @Test
  void notExistingStoryIdReturnsNull() {
    graphQlTester.document(query)
      .variable("storyId", 666)
      .execute()
      .path("story")
      .valueIsNull();
  }

  @Test
  void existingStoryIdReturnsStory() {
    given(storyRepository.findById(1L))
      .willReturn(Optional.of(story));

    GraphQlTester.Response response = graphQlTester.document(query)
      .variable("storyId", 1)
      .execute();
    response
      .path("story.id").entity(String.class).isEqualTo("1");
    response
      .path("story.title").entity(String.class).isEqualTo(story.getTitle());
  }

}
