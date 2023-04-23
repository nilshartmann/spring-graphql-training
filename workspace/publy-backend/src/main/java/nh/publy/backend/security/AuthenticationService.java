package nh.publy.backend.security;

import nh.publy.backend.domain.user.User;

import jakarta.validation.constraints.NotNull;

public interface AuthenticationService {
  User verify(@NotNull String token);
}
