package nh.publy.backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member getByUserId(String userId);

  @Query("SELECT id FROM Member m WHERE m.userId = :userId")
  Long getMemberIdForUserId(@Param("userId") String userId);

}
