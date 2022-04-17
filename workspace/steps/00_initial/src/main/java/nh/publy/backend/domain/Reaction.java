package nh.publy.backend.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reactions")
public class Reaction implements Identifiable {

  @Id
  @Column(name = "id")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  @NotNull
  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "given_by_id", referencedColumnName = "id")
  private Member givenBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "story_id")
  private Story story;

  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private ReactionType type;

  protected Reaction() {
  }

  public Reaction(Long id, Story story, Member givenBy, ReactionType reactionType) {
    this.id = id;
    this.story = story;
    this.createdAt = LocalDateTime.now();
    this.givenBy = givenBy;
    this.type = reactionType;
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

  public Member getGivenBy() {
    return givenBy;
  }

  public ReactionType getType() {
    return type;
  }

  public Story getStory() {
    return story;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reaction reaction = (Reaction) o;
    return Objects.equals(id, reaction.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
