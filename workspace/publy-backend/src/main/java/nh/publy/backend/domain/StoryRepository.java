package nh.publy.backend.domain;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoryRepository extends JpaRepository<Story, Long> {
  @Query(value = "SELECT nextval('story_ids');", nativeQuery = true)
  Long getNextStoryId();

  Window<Story> findAllBy(ScrollPosition scrollPosition, Limit limit, Sort sort);
}

