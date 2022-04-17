package nh.publy.backend.graphql;

public class StoryOrder implements StoryOrderBy {
  private final OrderDirection direction;
  private final StoryOrderByField field;

  public StoryOrder(OrderDirection direction, StoryOrderByField field) {
    this.direction = direction;
    this.field = field;
  }

  public OrderDirection getDirection() {
    return direction;
  }

  public StoryOrderByField getField() {
    return field;
  }

}
