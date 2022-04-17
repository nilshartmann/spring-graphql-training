package nh.publy.backend.graphql;

import nh.publy.backend.graphql.sql.SqlFilterCondition;

public class ReactionFilterCondition implements SqlFilterCondition {

  private final ReactionConditionField field;
  private final Object value;

  public ReactionFilterCondition(ReactionConditionField field, Object value) {
    this.field = field;
    this.value = value;
  }

  public ReactionConditionField getField() {
    return field;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public String getSqlFieldName() {
    return (switch (field) {
      case story -> "story_id";
      case givenBy -> "given_by_id";
    });
  }
}
