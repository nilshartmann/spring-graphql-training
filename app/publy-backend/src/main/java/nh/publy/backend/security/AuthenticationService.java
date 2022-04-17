package nh.publy.backend.security;

import nh.publy.backend.domain.user.User;

import javax.validation.constraints.NotNull;

public interface AuthenticationService {
  User verify(@NotNull String token);
}
