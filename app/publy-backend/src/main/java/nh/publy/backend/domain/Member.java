package nh.publy.backend.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "members")
public class Member implements Identifiable {

  @Id
  @Column(name = "id")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  @NotNull
  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  @NotNull
  @Column(name = "user_id")
  private String userId;

  @NotNull
  @Column(name = "profile_image")
  private String profileImage;

  @Column(name = "location")
  private String location;

  @Column(name = "bio")
  private String bio;

  @Column(name = "skills")
  private String skills;

  @Column(name = "currently_learning")
  private String currentlyLearning;

  @Column(name = "works")
  private String works;

  protected Member() {
  }

  public Member(Long id, String userId, String profileImage) {
    this.id = id;
    this.createdAt = LocalDateTime.now();
    this.userId = userId;
    this.profileImage = profileImage;
  }

  @Override
  public Long getId() {
    return id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getUserId() {
    return userId;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getSkills() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills = skills;
  }

  public String getCurrentlyLearning() {
    return currentlyLearning;
  }

  public void setCurrentlyLearning(String currentlyLearning) {
    this.currentlyLearning = currentlyLearning;
  }

  public LocalDateTime getJoined() {
    return this.getCreatedAt();
  }

  public String getWorks() {
    return works;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Member member = (Member) o;
    return id.equals(member.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Transient
  private String debug;

  public String getDebug() {
    return debug;
  }

  public void setDebug(String debug) {
    this.debug = debug;
  }
}
