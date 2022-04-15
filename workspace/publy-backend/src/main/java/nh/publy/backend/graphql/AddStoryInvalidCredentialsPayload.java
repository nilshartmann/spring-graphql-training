package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;

public record AddStoryInvalidCredentialsPayload(String message, Member member) {
}
