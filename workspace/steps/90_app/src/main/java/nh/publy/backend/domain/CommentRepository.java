package nh.publy.backend.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

  @Query(value = "SELECT nextval('comment_ids');", nativeQuery = true)
  Long getNextCommentId();

  public List<Comment> findAllByStoryId(Long storyId);


}
