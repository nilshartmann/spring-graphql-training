package nh.graphql.publy.userservice;

import javax.validation.constraints.NotNull;

public record PublicKeyResponse(@NotNull String publicKey) {
}
