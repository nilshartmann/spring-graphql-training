package nh.graphql.publy.userservice;

import javax.validation.constraints.NotNull;

public record VerifyRequest(@NotNull String token) {
}
