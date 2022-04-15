package nh.publy.backend.graphql;

import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.*;

public enum StoryConditionField {
  writtenBy("written_by_id"),
  /**
   * note: as StoryCondition in graphql takes a String as argument, no coercing
   * from String -> LocalDateTime in Java is done
   *
   * We have to cast the string ourself (and doing it here in the database)
   */
  newerThan("created_at",tablePrefix -> format("%s.created_at > CAST(:condition AS timestamp)", tablePrefix)),
  tag("tags", (tablePrefix) -> format(":condition = ANY(%s.tags)", tablePrefix));

  private final Optional<Function<String, String>> conditionBuilder;
  private final String sqlFieldName;

  StoryConditionField(String sqlFieldName) {
    this(sqlFieldName, null);
  }

  StoryConditionField(String sqlFieldName, Function<String, String> conditionBuilder) {
    this.sqlFieldName = sqlFieldName;
    this.conditionBuilder = Optional.ofNullable(conditionBuilder);
  }

  public String getSqlFieldName() {
    return sqlFieldName;
  }

  public Optional<String> asWhereCondition(String tablePrefix) {
    return conditionBuilder.map(b -> b.apply(tablePrefix));
  }


}

