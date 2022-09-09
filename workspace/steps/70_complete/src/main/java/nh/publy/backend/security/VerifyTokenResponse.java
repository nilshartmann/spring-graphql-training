package nh.publy.backend.security;

import nh.publy.backend.domain.user.User;

import java.util.Objects;

public final class VerifyTokenResponse {
  private final User user;

  VerifyTokenResponse(User user) {
    this.user = user;
  }

  public User user() {
    return user;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (VerifyTokenResponse) obj;
    return Objects.equals(this.user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user);
  }

  @Override
  public String toString() {
    return "VerifyTokenResponse[" +
      "user=" + user + ']';
  }

}
