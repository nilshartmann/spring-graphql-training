package nh.publy.backend.graphql;

import java.util.Base64;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

public class NodeId {

  private NodeType nodeType;
  private Long id;

  public NodeId(String id) {
    try {
      String[] parts = decodeFromBase64(id).split(":");
      if (parts.length != 2) {
        throw new InvalidIdFormatException(id);
      }

      this.nodeType = NodeType.valueOf(parts[0]);
      this.id = Long.parseLong(parts[1]);

    } catch (Exception ex) {
      throw new InvalidIdFormatException(id);
    }
  }

  public NodeId(Long id, NodeType nodeType) {
    this.id = id;
    this.nodeType = nodeType;
  }

  private void expectType(NodeType expectedNodeType) {
    if (!this.nodeType.equals(expectedNodeType)) {
      throw new IllegalStateException(String.valueOf(this.id));
    }
  }

  public static NodeId forStory(Long storyId) {
    return new NodeId(storyId, NodeType.story);
  }

  public static NodeId forComment(Long commentId) {
    return new NodeId(commentId, NodeType.comment);
  }

  public static NodeId forReaction(Long reactionId) {
    return new NodeId(reactionId, NodeType.reaction);
  }

  public static NodeId forMember(Long memberId) { return new NodeId(memberId, NodeType.member); }

  /** If this NodeId points to a Story Node, return the id, otherwise throws exception */
  public Long getStoryId() {
    expectType(NodeType.story);
    return id;
  }

  /** If this NodeId points to a Member Node, return the id, otherwise throws exception */
  public Long getMemberId() {
    expectType(NodeType.member);
    return id;
  }

  /** If this NodeId points to a Comment Node, return the id, otherwise throws exception */
  public Long getCommentId() {
    expectType(NodeType.comment);
    return id;
  }

  /** If this NodeId points to a Reaction Node, return the id, otherwise throws exception */
  public Long getReactionId() {
    expectType(NodeType.reaction);
    return id;
  }

  public Long getId() {
    return id;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public String toString() {
    return encodeToBase64(this.nodeType.name() + ":" + getId());
  }

  private static String decodeFromBase64(String base64String) {
    return new String(Base64.getDecoder().decode(base64String));
  }

  private static String encodeToBase64(String rawString) {
    return new String(Base64.getEncoder().encodeToString(rawString.getBytes()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodeId nodeId = (NodeId) o;
    return nodeType == nodeId.nodeType && id.equals(nodeId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeType, id);
  }
}
