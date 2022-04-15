package nh.publy.backend.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {

  Optional<Story> findFirstByOrderByCreatedAtDesc();

  @EntityGraph(attributePaths = {"writtenBy"})
  public Optional<Story> findFirstWithAuthorByOrderByCreatedAtDesc();

  @EntityGraph(attributePaths = {"writtenBy"})
  public Optional<Story> findStoryAndAuthorById(Long id);

  Page<Story> findAllByCreatedAtGreaterThan(LocalDateTime date, Pageable pageable);

  Page<Story> findAllByWrittenById(Long memberId, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT * FROM stories WHERE :tagName = ANY(tags) ORDER BY created_at DESC LIMIT 5")
  List<Story> getNewestStoriesWithTag(@Param("tagName") String tagName);

  @Query(value = "SELECT nextval('story_ids');", nativeQuery = true)
  Long getNextStoryId();

  @Query(nativeQuery = true, value = """
        WITH
        	__all__tags AS( SELECT DISTINCT ON (tag)
               unnest(s.tags) tag, s.created_at, s.id
        		FROM stories s
        		ORDER BY tag, s.created_at DESC, s.id),
        		
        	__newest_tags__ AS (SELECT DISTINCT ON (id) tag, created_at FROM __all__tags)
        	
        	SELECT tag FROM __newest_tags__ ORDER BY __newest_tags__.created_at DESC LIMIT 5
    """)
  List<String> findTopTags();
}

