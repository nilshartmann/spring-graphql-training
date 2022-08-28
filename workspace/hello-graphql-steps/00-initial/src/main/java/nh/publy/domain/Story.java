package nh.publy.domain;


public class Story {

  private Long id;
  private String title;
  private String body;
  private Member writtenBy;

  public Story(Long id, String title, String body, Member writtenBy) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.writtenBy = writtenBy;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public Member getWrittenBy() {
    return writtenBy;
  }
}
