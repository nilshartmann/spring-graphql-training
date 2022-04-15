package nh.publy.backend.graphql;

/**
 * Published when either a new Comment or a new Reaction has been added to a Story.
 *
 * @param storyId Points to the story that the feedback received
 * @param feedbackNodeId Points to the new feedback object (nodeType: comment or reaction) that
 *                       has been created
 */
public record FeedbackReceivedEvent(NodeId storyId, NodeId feedbackNodeId) {
}
