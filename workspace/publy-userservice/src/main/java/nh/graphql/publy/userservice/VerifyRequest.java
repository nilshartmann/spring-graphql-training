package nh.graphql.publy.userservice;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public final class VerifyRequest {
  private final @NotNull String token;

  VerifyRequest(String token) {
    this.token = token;
  }

  public String token() {
    return token;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (VerifyRequest) obj;
    return Objects.equals(this.token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return "VerifyRequest[" +
      "token=" + token + ']';
  }

}
