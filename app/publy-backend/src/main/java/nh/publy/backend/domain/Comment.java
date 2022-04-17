package nh.publy.backend.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment implements Identifiable {

  @Id
  @Column(name = "id")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  @NotNull
  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "story_id")
  @NotNull
  private Story story;

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "written_by_id", referencedColumnName = "id")
  private Member writtenBy;

  @NotNull
  @Column(name = "content")
  private String content;

  protected Comment() {
  }

  public Comment(Long id, Story story, Member createdBy, String content) {
    this.id = id;
    this.story = story;
    this.createdAt = LocalDateTime.now();
    this.writtenBy = createdBy;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public Integer getVersion() {
    return version;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Member getWrittenBy() {
    return writtenBy;
  }

  public String getContent() {
    return content;
  }

  public Story getStory() {
    return story;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
