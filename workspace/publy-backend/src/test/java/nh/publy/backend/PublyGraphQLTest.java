package nh.publy.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;


@GraphQlTest
public class PublyGraphQLTest extends AbstractPublyGraphQLTest {

  @Test
  void storyByIdShouldWork() {
    given_storyOneIsReturned();

    // language=GraphQL
    GraphQlTester.Response response = graphQlTester.document("query { story(storyId: 1) { id title } }")
      .execute();

    response
      .path("story.id").entity(String.class).isEqualTo("1")
      .path("story.title").entity(String.class).isEqualTo("A great Story #1");
  }

  @Test
  void storyByIdWithUnknownIdShouldReturnNull() {

    // language=GraphQL
    GraphQlTester.Response response = graphQlTester.document("query { story(storyId: 666) { id title } }")
      .execute();

    response
      .path("story").valueIsNull();
  }

}
