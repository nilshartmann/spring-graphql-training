package nh.publy.backend.domain;

public class CommentCreationFailedException extends RuntimeException {
  public CommentCreationFailedException(String msg) {
    super(msg);
  }
}
