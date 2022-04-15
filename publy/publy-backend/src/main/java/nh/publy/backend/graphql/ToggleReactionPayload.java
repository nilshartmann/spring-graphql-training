package nh.publy.backend.graphql;

import nh.publy.backend.domain.Story;

public record ToggleReactionPayload(Story updatedStory) {
}
