package nh.publy.backend.graphql;

public interface StoryOrderBy {
  public OrderDirection getDirection();

  public StoryOrderByField getField();
}
