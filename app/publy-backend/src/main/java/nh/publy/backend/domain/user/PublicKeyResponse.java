package nh.publy.backend.domain.user;

import jakarta.validation.constraints.NotNull;

public record PublicKeyResponse(@NotNull String publicKey) {
}
