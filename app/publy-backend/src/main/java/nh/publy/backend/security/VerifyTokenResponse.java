package nh.publy.backend.security;

import nh.publy.backend.domain.user.User;

import jakarta.validation.constraints.NotNull;

public record VerifyTokenResponse(@NotNull User user) {
}
