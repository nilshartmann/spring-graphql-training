package nh.graphql.publy.userservice;

import jakarta.validation.constraints.NotNull;

public record VerifyRequest(@NotNull String token) {
}
