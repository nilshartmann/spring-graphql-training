package nh.publy.domain;

import java.util.List;
import java.util.Optional;

public class StoryRepository {

  private static final Member member1 = new Member(1L, "Member 1", "Password One");
  private static final Member member2 = new Member(2L, "Member 2", "Password Two");
  private static final Member member3 = new Member(3L, "Member 3", "Password Three");

  public static List<Story> exampleStories = List.of(
    new Story(1L, "Story 1", "Body Story 1", member1),
    new Story(2L, "Story 2", "Body Story 2", member2),
    new Story(3L, "Story 3", "Body Story 3", member3),
    new Story(4L, "Story 4", "Body Story 4", member1),
    new Story(5L, "Story 5", "Body Story 5", member2)
  );

  public List<Story> findAllStories() {
    return List.copyOf(exampleStories);
  }

  public Optional<Story> findStoryById(Long id) {
    return exampleStories.stream()
      .filter(s -> s.getId().equals(id))
      .findFirst();
  }
}
