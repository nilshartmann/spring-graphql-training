package nh.publy.backend.security;

import java.util.Objects;

public final class VerifyTokenRequest {
  private final String token;

  VerifyTokenRequest(String token) {
    this.token = token;
  }

  public String token() {
    return token;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (VerifyTokenRequest) obj;
    return Objects.equals(this.token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return "VerifyTokenRequest[" +
      "token=" + token + ']';
  }

}
