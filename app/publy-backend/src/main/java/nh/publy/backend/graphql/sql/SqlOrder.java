package nh.publy.backend.graphql.sql;

import nh.publy.backend.graphql.OrderDirection;

import static java.lang.String.*;

public class SqlOrder {

  private final String sqlFieldName;
  private final boolean external;
  private final OrderDirection direction;

  public SqlOrder(String sqlFieldName, boolean external, OrderDirection direction) {
    this.sqlFieldName = sqlFieldName;
    this.external = external;
    this.direction = direction;
  }

  public SqlOrder(String sqlFieldName, OrderDirection direction) {
    this(sqlFieldName, false, direction);
  }

  public String getSqlFieldName() {
    return sqlFieldName;
  }

  public OrderDirection getDirection() {
    return direction;
  }

  public boolean isExternal() {
    return external;
  }

  public String toSql(String tablePrefix, boolean reverseDirection) {
    String sqlOrderDirection = getDirection().isAsc() ?
      reverseDirection ? "DESC" : "ASC" : reverseDirection ? "ASC" : "DESC";

    String sqlOrder = format("%s %s",
      isExternal() ? getSqlFieldName() : tablePrefix + "." + getSqlFieldName(),
      sqlOrderDirection);

    return sqlOrder;
  }

}
