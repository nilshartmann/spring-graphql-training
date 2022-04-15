package nh.publy.backend.graphql;

import graphql.relay.DefaultEdge;
import graphql.relay.Edge;
import nh.publy.backend.domain.Comment;
import nh.publy.backend.domain.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

public class OnNewCommentEvent {
  private static final Logger log = LoggerFactory.getLogger(OnNewCommentEvent.class);

  private final CommentRepository commentRepository;

  private final Long storyId;
  private final Long commentId;

  protected Comment comment;

  public OnNewCommentEvent(CommentRepository commentRepository,
                           Long storyId,
                           Long commentId) {
    this.commentRepository = commentRepository;
    this.storyId = storyId;
    this.commentId = commentId;
  }

  protected Comment loadComment() {
    if (comment == null) {
      log.info("Loading comment with id '" + commentId + "'");
      comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("No comment with id '" + commentId + "' found"));
    } else {
      log.info("Comment with id alread loaded '{}'", commentId);
    }

    return comment;
  }

  public Long getStoryId() {
    return storyId;
  }

  public Long getCommentId() {
    return commentId;
  }

  public Edge<Comment> getNewCommentEdge() {
    Comment comment = loadComment();
    var result = new DefaultEdge<>(
      comment,
      PublyCursor.forObject(comment));
    return result;
  }

  public Comment getNewComment() {
    return loadComment();
  }

}
