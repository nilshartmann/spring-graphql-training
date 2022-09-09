package nh.publy.backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoryRepository extends JpaRepository<Story, Long> {
  @Query(value = "SELECT nextval('story_ids');", nativeQuery = true)
  Long getNextStoryId();
}

