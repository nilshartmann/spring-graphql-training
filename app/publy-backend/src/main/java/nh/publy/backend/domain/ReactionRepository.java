package nh.publy.backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
  @Query(value = "SELECT type, count(*) AS totalCount FROM reactions WHERE story_id = :storyId GROUP BY type ORDER BY type", nativeQuery = true)
  List<ReactionCount> countByReactionType(@Param("storyId") Long storyId);

  @Query(value = "SELECT type FROM reactions WHERE story_id = :storyId AND given_by_id = :memberId GROUP BY type ORDER BY type", nativeQuery = true)
  List<ReactionType> reactionsForStoryAndUser(@Param("storyId") Long storyId, @Param("memberId") Long memberId);

  @Query(value = "SELECT nextval('reaction_ids');", nativeQuery = true)
  public Long getNextReactionId();
}
