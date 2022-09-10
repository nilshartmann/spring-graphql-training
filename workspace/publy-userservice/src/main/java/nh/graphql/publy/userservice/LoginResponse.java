package nh.graphql.publy.userservice;

import java.util.Objects;

public final class LoginResponse {
  private final String token;

  LoginResponse(String token) {
    this.token = token;
  }

  public String token() {
    return token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (LoginResponse) obj;
    return Objects.equals(this.token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return "LoginResponse[" +
      "token=" + token + ']';
  }

}
