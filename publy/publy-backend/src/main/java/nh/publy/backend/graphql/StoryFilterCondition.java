package nh.publy.backend.graphql;

import nh.publy.backend.domain.Member;
import nh.publy.backend.graphql.sql.SqlFilterCondition;

public class StoryFilterCondition implements SqlFilterCondition {

  private final StoryConditionField field;
  private final Object value;

  public StoryFilterCondition(StoryConditionField field, Object value) {
    this.field = field;
    this.value = value;
  }

  public static StoryFilterCondition writtenByMember(Member member) {
    return new StoryFilterCondition(
      StoryConditionField.writtenBy,
      new NodeId(member.getId(), NodeType.member)
    );
  }

  public StoryConditionField getField() {
    return field;
  }

  public Object getValue() {
    if (field == StoryConditionField.writtenBy) {
      return NodeId.parse(value)
        .getMemberId();
    }

    return value;
  }

  @Override
  public String getSqlFieldName() {
    return field.getSqlFieldName();
  }

  @Override
  public String getAsWhereCondition(String tablePrefix) {
    return field.asWhereCondition(tablePrefix)
      .orElseGet(() -> SqlFilterCondition.super.getAsWhereCondition(tablePrefix));
  }
}
