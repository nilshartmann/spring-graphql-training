package nh.publy.backend.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, Long> {

  @Query(value = "SELECT nextval('comment_ids');", nativeQuery = true)
  public Long getNextCommentId();

  @Query(value = "SELECT count(*) FROM comments c WHERE c.story_id = :storyId", nativeQuery = true)
  public Long countCommentsForStory(@Param("storyId") Long storyId);
}
