package nh.publy.backend;

import nh.publy.backend.domain.Member;
import nh.publy.backend.domain.Story;
import nh.publy.backend.domain.StoryRepository;
import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

public class AbstractPublyGraphQLTest {

  @Autowired
  GraphQlTester graphQlTester;

  @MockBean
  StoryRepository storyRepository;

  @MockBean
  UserService userService;

  // ---------- Some sample data you can use in your own test (or create your own) -----------------------------------

  final Member member = new Member(1L, "U1", "avatar.png");
  final User user = new User("U1", "susi", "Susi Mueller", "susi@example.com" );
  final Story story1 = new Story(1L, member, "A great Story #1", new String[]{"graphql"}, "Lorem ipsum");
  final Story story2 = new Story(2L, member, "A second great story", new String[]{"graphql"}, "Lirum larum loeffelstiel");

  // ---------- Some mock repository configs your can use (or setup your own) ----------------------------------------
  void given_storyOneIsReturned() {
    given(storyRepository.findById(1L)).willReturn(Optional.of(story1));
  }

  void given_listOfStoriesIsReturned() {
    given(storyRepository.findAll()).willReturn(List.of(story1, story2));
  }

  void given_userServiceReturnsUser1() {
    given(userService.findUser("U1")).willReturn(Mono.just(user));
  }
}
