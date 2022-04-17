package nh.graphql.publy.userservice;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public final class LoginRequest {

  @NotNull public final String username;
  @NotNull public final String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String username() {
    return this.username;
  }

  public String password() {
    return this.password;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj != null && obj.getClass() == this.getClass();
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public String toString() {
    return "LoginRequest[]";
  }
}
