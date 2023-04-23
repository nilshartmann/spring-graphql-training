package nh.graphql.publy.userservice;


import jakarta.validation.constraints.NotNull;

public record PublicKeyResponse(@NotNull String publicKey) {
}
