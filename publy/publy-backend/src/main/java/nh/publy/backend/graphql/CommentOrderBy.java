package nh.publy.backend.graphql;

public interface CommentOrderBy {
  public OrderDirection getDirection();

  public CommentOrderByField getField();
}
