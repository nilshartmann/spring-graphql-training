package nh.publy.backend.domain;

import java.util.List;
import java.util.Objects;

public record TopTags(String tagName, List<Story> stories) {

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (TopTags) obj;
    return Objects.equals(this.tagName, that.tagName) &&
      Objects.equals(this.stories, that.stories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagName, stories);
  }

  @Override
  public String toString() {
    return "TopTags[" +
      "tagName=" + tagName + ", " +
      "stories=" + stories + ']';
  }

}
