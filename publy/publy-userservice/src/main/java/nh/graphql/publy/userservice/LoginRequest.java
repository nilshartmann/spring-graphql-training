package nh.graphql.publy.userservice;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public record LoginRequest(@NotNull String username, @NotNull String password) {
}
