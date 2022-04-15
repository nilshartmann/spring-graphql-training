package nh.publy.backend.graphql;

import nh.publy.backend.graphql.sql.SqlFilterCondition;

public class CommentFilterCondition implements SqlFilterCondition {

  private final CommentConditionField field;
  private final Object value;

  public CommentFilterCondition(CommentConditionField field, Object value) {
    this.field = field;
    this.value = value;
  }

  public CommentConditionField getField() {
    return field;
  }

  public Object getValue() {
    if (value instanceof String v) {
      NodeId nodeId = new NodeId(v);
      return nodeId.getId();
    }

    return value;
  }

  @Override
  public String getSqlFieldName() {
    return (switch (field) {
      case story -> "story_id";
      case writtenBy -> "written_by_id";
    });
  }

  @Override
  public String toString() {
    return "CommentFilterCondition{" +
      "field=" + field +
      ", value=" + value +
      '}';
  }
}
