package nh.publy.backend.graphql.sql;

import static java.lang.String.*;

public interface SqlFilterCondition {
  public String getSqlFieldName();

  public Object getValue();

  default String getAsWhereCondition(String tablePrefix) {
    return format("%s.%s = :condition", tablePrefix, this.getSqlFieldName());
  }

}
