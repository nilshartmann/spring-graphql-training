package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;

import java.util.Optional;

public record AddStoryInvalidCredentialsPayload(String message, Optional<Member> member) {
}
