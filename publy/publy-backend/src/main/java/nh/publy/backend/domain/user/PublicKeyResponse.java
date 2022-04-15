package nh.publy.backend.domain.user;

import javax.validation.constraints.NotNull;

public record PublicKeyResponse(@NotNull String publicKey) {
}
