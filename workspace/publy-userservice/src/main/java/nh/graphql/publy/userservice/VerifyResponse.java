package nh.graphql.publy.userservice;

import java.util.Objects;

public final class VerifyResponse {
  public final User user;

  VerifyResponse(User user) {
    this.user = user;
  }

  public User user() {
    return user;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (VerifyResponse) obj;
    return Objects.equals(this.user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user);
  }

  @Override
  public String toString() {
    return "VerifyResponse[" +
      "user=" + user + ']';
  }

}
