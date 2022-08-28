package nh.publy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest {

  private final static String SCHEMA_LOCATION = "/graphql/hello-graphql-java.graphqls";

  private GraphQLSchemaAssertions.GraphQLSchemaAssertion assertGraphQLSchema() {
    return GraphQLSchemaAssertions.assertGraphQLSchema(SCHEMA_LOCATION);
  }

  @Test
  void memberTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Member")
          .withField("id").ofType("ID!")
          .and().withField("profileImage").ofType("String!")
    ;
  }

  @Test
  void storyTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Story")
        .withField("id").ofType("ID!")
        .and().withField("title").ofType("String!")
        .and().withField("body").ofType("String!")
        .and().withField("writtenBy").ofType("Member!")
        .and().withField("excerpt").ofType("String!")
    ;
  }


  @Test
  void queryTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Query")
        .withField("story").ofType("Story")
          .withDescription()
          .withArgument("storyId").ofType("ID!")
        .and()
      .and()
        .withField("stories").ofType("[Story!]!")
      ;
  }

}
