package nh.publy.backend.graphql;

import java.util.Base64;

import static nh.publy.backend.graphql.DemoConfig.useComplexNodeIds;

public class NodeId {
  private final static boolean useBase64 = true;
  private NodeType nodeType;
  private Long id;

  public static NodeId parse(Object value) {
    if (value == null) {
      throw new IllegalArgumentException("value of a NodeId must not be null");
    }

    var id = String.valueOf(value);

    if (!useComplexNodeIds) {
      long parsedId;
      try {
        parsedId = Long.parseLong(id);
      } catch (Exception ex) {
        throw new InvalidIdFormatException(id);
      }
      return new NodeId(parsedId, null);
    } else {
      try {
        String decoded = decode(id);
        String[] parts = decoded.split(":");
        if (parts.length != 2) {
          throw new InvalidIdFormatException(id);
        }

        String typeName = parts[0];
        var nodeType = NodeType.valueOf(typeName);
        String rawValue = parts[1];
        var parsedId = Long.parseLong(rawValue);

        return new NodeId(parsedId, nodeType);

      } catch (Exception ex) {
        throw new InvalidIdFormatException(id);
      }
    }
  }

//  public NodeId(String id) {
//    if (!useComplexNodeIds) {
//      this.nodeType = null;
//      try {
//        this.id = Long.parseLong(id);
//      } catch (Exception ex) {
//        throw new InvalidIdFormatException(id);
//      }
//    } else {
//      try {
//        String decoded = decode(id);
//        String[] parts = decoded.split(":");
//        if (parts.length != 2) {
//          throw new InvalidIdFormatException(id);
//        }
//
//        String typeName = parts[0];
//        this.nodeType = NodeType.valueOf(typeName);
//        String rawValue = parts[1];
//        this.id = parseRawValue(rawValue);
//
//      } catch (Exception ex) {
//        throw new InvalidIdFormatException(id);
//      }
//    }
//  }

  public NodeId(Long id, NodeType nodeType) {
    this.id = id;
    this.nodeType = nodeType;
  }

  private void expectType(NodeType expectedNodeType) {
    if (!useComplexNodeIds) {
      return;
    }
    if (!this.nodeType.equals(expectedNodeType)) {
      throw new InvalidIdFormatException(String.valueOf(this.id));
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

  private static String decode(String base64String) {
    if (useBase64) {
      return new String(Base64.getDecoder().decode(base64String));
    }
    return base64String;
  }

  public String toString() {
    if (!useComplexNodeIds) {
      return getId().toString();
    }

    String idString = this.nodeType.name() + ":" + getId();
    if (useBase64) {
      return new String(Base64.getEncoder().encodeToString(idString.getBytes()));
    }

    return idString;
  }
}
