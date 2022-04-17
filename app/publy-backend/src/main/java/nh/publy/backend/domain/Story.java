package nh.publy.backend.domain;

import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "stories")
public class Story implements Identifiable {

  private static final Logger log = LoggerFactory.getLogger(Story.class);

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
  @JoinColumn(name = "written_by_id", referencedColumnName = "id")
  private Member writtenBy;

  @NotNull
  @Size(min = 10)
  @Column(name = "title")
  private String title;

  @Column(name = "tags")
  @NotNull
  @Size(min = 1)
  @Type(type = "nh.publy.backend.util.PostgreSqlStringArrayType")
  private String[] tags;

  @NotNull
  @Size(min = 10)
  @Column(name = "body_markdown")
  private String bodyMarkdown;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "story", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Reaction> reactions = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "story", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Comment> comments = new HashSet<Comment>();

  protected Story() {
  }

  public Story(Long id, Member writtenBy, String title, String[] tags, String body) {
    this.id = id;
    this.writtenBy = writtenBy;
    this.createdAt = LocalDateTime.now();
    this.title = title;
    this.tags = tags;
    this.bodyMarkdown = body;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBodyMarkdown() {
    return bodyMarkdown;
  }

  public void setBodyMarkdown(String body) {
    this.bodyMarkdown = body;
  }

  public String[] getTags() {
    return tags;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public Set<Reaction> getReactions() {
    return reactions;
  }

  public boolean removeReactionByUser(Member member, ReactionType type) {
    for (Iterator<Reaction> iterator = reactions.iterator(); iterator.hasNext(); ) {
      Reaction reaction = iterator.next();
      if (reaction.getType() == type && member.equals(reaction.getGivenBy())) {
        log.info("Remove Reaction {} from Story {} ({}) for memeber with id {}", reaction.getId(), getId(), getVersion(), member.getId());
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  public void addReaction(Long reactionId, Member member, ReactionType reactionType) {
    Reaction reaction = new Reaction(reactionId, this, member, reactionType);
    this.reactions.add(reaction);
  }
}
