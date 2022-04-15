package nh.publy.backend.graphql;

public record AddCommentInput(String content, NodeId storyId) {
}
