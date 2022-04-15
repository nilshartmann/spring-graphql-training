package nh.publy.backend.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {

  @Query(value = "SELECT nextval('story_ids');", nativeQuery = true)
  public Long getNextStoryId();

  public Optional<Story> findFirstByOrderByCreatedAtDesc();

  @EntityGraph(attributePaths = {"writtenBy"})
  public Optional<Story> findFirstWithAuthorByOrderByCreatedAtDesc();

  public List<Story> findAllBy(Pageable pageRequest);

  @EntityGraph(attributePaths = {"writtenBy"})
  public Optional<Story> findStoryAndAuthorById(Long id);

  @Query(nativeQuery = true, value = """
        WITH
        	__all__tags AS( SELECT DISTINCT ON (tag)
               unnest(s.tags) tag, s.created_at, s.id
        		FROM stories s
        		ORDER BY tag, s.created_at DESC, s.id),
        		
        	__newest_tags__ AS (SELECT DISTINCT ON (id) tag, created_at FROM __all__tags)
        	
        	SELECT tag FROM __newest_tags__ ORDER BY __newest_tags__.created_at DESC LIMIT 5
    """)
  public List<String> findTopTags();


  @Query(nativeQuery = true, value = """
        WITH
        	__all__tags AS( SELECT DISTINCT ON (tag)
               unnest(s.tags) tag, s.created_at, s.id
        		FROM stories s
        		WHERE s.written_by_id = :memberId
        		ORDER BY tag, s.created_at DESC, s.id),
        		
        	__newest_tags__ AS (SELECT DISTINCT ON (id) tag, created_at FROM __all__tags)
        	
        	SELECT tag FROM __newest_tags__ ORDER BY __newest_tags__.created_at DESC LIMIT 5
    """)
  public List<String> findTopTagsFor(@Param("memberId") Long memberId);


  @Query(nativeQuery = true, value = "SELECT * FROM stories WHERE :tagName = ANY(tags) ORDER BY created_at DESC LIMIT 5")
  public List<Story> getNewestStoriesWithTag(@Param("tagName") String tagName);
}

