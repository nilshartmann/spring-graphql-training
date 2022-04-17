package nh.publy.backend.graphql;


import nh.publy.backend.domain.*;
import nh.publy.backend.domain.user.UserService;
import nh.publy.backend.graphql.runtime.PublyGraphQLConfig;
import nh.publy.backend.graphql.runtime.PublyGraphQLExceptionResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@GraphQlTest(controllers = StoryGraphQLController.class)
@Import({PublyGraphQLConfig.class, PublyGraphQLExceptionResolver.class})
public class PublyGraphQLControllerTest {
  @MockBean StoryRepository storyRepository;
  @MockBean MarkdownService markdownService;
  @MockBean MemberRepository memberRepository;
  @MockBean PublyDomainService publyDomainService;
  @MockBean CommentRepository commentRepository;
  @MockBean CommentEventPublisher commentEventPublisher;
  @MockBean ReactionRepository reactionRepository;
  @MockBean UserService userService;
  @MockBean EntityManagerFactory entityManagerFactory;

  @Autowired
  GraphQlTester graphQlTester;

  // language=GraphQL
  private final String query = """
  query ($storyId: ID!) {
    story(id: $storyId) {
      id title
    }
  }
""";

  private final NodeId storyNodeId = NodeId.forStory(1L);

  private final Story story = new Story(1L,
    null, "Hurray!", new String[]{"graphql"}, "Great Story!");

  @Test
  void notExistingStoryIdReturnsNull() {
    graphQlTester.document(query)
      .variable("storyId", storyNodeId.toString())
      .execute()
      .path("story")
      .valueIsNull();
  }

  @Test
  void existingStoryIdReturnsStory() {

    given(storyRepository.findById(1L))
      .willReturn(Optional.of(story));

    GraphQlTester.Response response = graphQlTester.document(query)
      .variable("storyId", storyNodeId.toString())
      .execute();
    response
      .path("story.id").entity(String.class).isEqualTo(storyNodeId.toString());
    response
      .path("story.title").entity(String.class).isEqualTo(story.getTitle());
  }

  @Test
  void invalidNodeIdReturnsError() {
    graphQlTester.document(query)
      .variable("storyId", "xxx")
      .execute()
      .errors().satisfy(l -> {
        assertThat(l).hasSize(1);
        assertThat(l.get(0).getErrorType().toString()).isEqualTo("invalidNodeId");
      });
  }

}
