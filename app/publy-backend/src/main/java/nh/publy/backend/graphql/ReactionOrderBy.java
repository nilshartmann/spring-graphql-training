package nh.publy.backend.graphql;

public interface ReactionOrderBy {
  public OrderDirection getDirection();

  public ReactionOrderByField getField();
}
