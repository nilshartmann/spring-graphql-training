package nh.publy.backend.graphql;

import nh.publy.backend.domain.user.User;
import nh.publy.backend.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/stories-test-data.sql")
@AutoConfigureHttpGraphQlTester
public class PublyGraphQLControllerWebTest {

  @Container
  static JdbcDatabaseContainer<?> database = new PostgreSQLContainer<>("postgres:14")
    .withDatabaseName("springboot")
    .withPassword("springboot")
    .withUsername("springboot")
    .withInitScript("schema.sql");

  @DynamicPropertySource
  static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
    propertyRegistry.add("spring.datasource.password", database::getPassword);
    propertyRegistry.add("spring.datasource.username", database::getUsername);

    propertyRegistry.add("publy.dummyContentGenerator.enable-auto", () -> "false");
  }

  @MockBean
  AuthenticationService authenticationService;

  @Autowired
  private WebGraphQlTester graphQlTester;

  // language=GraphQL
  private String query = """
  mutation ($storyId: ID!) {
    toggleReaction(input: {storyId: $storyId, reactionType: like}) {
      updatedStory {
        id      
      }
    }
  }
""";

  private final String storyNodeId = NodeId.forStory(1L).toString();

  @Test
  void toggleReactionUnauthorized() {
    graphQlTester.document(query)
      .variable("storyId", storyNodeId)
      .execute()
      .errors()
      .satisfy(errors -> {
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.UNAUTHORIZED);
      });
  }

  private final User user = new User("U15", "murphy", "Ethelyn Murphy", "ethelyn.murphy@example.com", "ROLE_USER", "ROLE_GUEST");

  @Test
  @DirtiesContext
  void toggleReactionAuthorized() {
    given(authenticationService.verify("user-mock-token"))
      .willReturn(user);

    graphQlTester
      .mutate()
      .header("Authorization", "Bearer user-mock-token")
      .build()
      .document(query)
      .variable("storyId", storyNodeId)
      .executeAndVerify();
  }
}
