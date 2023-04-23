package nh.graphql.publy.userservice;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record LoginRequest(@NotNull String username, @NotNull String password) {
}
