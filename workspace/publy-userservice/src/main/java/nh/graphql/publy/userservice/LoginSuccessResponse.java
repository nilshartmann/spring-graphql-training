package nh.graphql.publy.userservice;

import java.util.Objects;

public final class LoginSuccessResponse {
  private final String token;

  LoginSuccessResponse(String token) {
    this.token = token;
  }

  public String token() {
    return token;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (LoginSuccessResponse) obj;
    return Objects.equals(this.token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return "LoginSuccessResponse[" +
      "token=" + token + ']';
  }

}
