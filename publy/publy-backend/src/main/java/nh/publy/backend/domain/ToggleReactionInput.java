package nh.publy.backend.domain;

import nh.publy.backend.graphql.NodeId;

public record ToggleReactionInput(NodeId storyId, ReactionType reactionType) {
}
