package nh.publy.backend.graphql.sql;

import nh.publy.backend.graphql.OrderDirection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;
import java.util.StringJoiner;

import static java.lang.String.*;

public class SelectNodesSqlBuilder {
  private final String tableName;
  private final Class<?> entityType;
  private final SqlOrder customSqlOrder;
  private final Optional<? extends SqlFilterCondition> condition;
  private final boolean first;
  private final Optional<Long> after;
  private final Optional<Long> before;
  private final int limit;

  public SelectNodesSqlBuilder(String tableName, Class<?> entityType, SqlOrder customSqlOrder, Optional<? extends SqlFilterCondition> condition, boolean first, Optional<Long> after, Optional<Long> before, int limit) {
    this.tableName = tableName;
    this.entityType = entityType;
    this.customSqlOrder = customSqlOrder;
    this.condition = condition;
    this.first = first;
    this.after = after;
    this.before = before;
    this.limit = limit;
  }

  public Query buildDataQuery(EntityManager entityManager) {
    String selectFrom = getSelectFrom();
    String where = getWhere(true);
    String orderBy = getOrderBy();

    String sqlData = format("%s %s %s LIMIT :count",
      selectFrom,
      where,
      orderBy);

    String sql = addReverseOrder(sqlData);

    Query nativeQuery = entityManager.createNativeQuery(sql, getEntityType());

    nativeQuery.setParameter("count", limit);
    condition.ifPresent(c -> nativeQuery.setParameter("condition", c.getValue()));
    after.ifPresent(afterId -> nativeQuery.setParameter("after", afterId));
    before.ifPresent(beforeId -> nativeQuery.setParameter("before", beforeId));

    return nativeQuery;
  }

  public Query buildCountQuery(EntityManager entityManager) {
    String selectFrom = getSelectFrom();
    String where = getWhere(false);

    String sqlData = format("%s %s",
      selectFrom,
      where);

    String sql = addCountQuery(sqlData);

    Query nativeQuery = entityManager.createNativeQuery(sql, Integer.class);
    condition.ifPresent(c -> nativeQuery.setParameter("condition", c.getValue()));
    return nativeQuery;
  }

  protected String getSelectFrom() {
    return format("SELECT %s.* FROM %s %s",
      getTablePrefix(),
      getTableName(),
      getTablePrefix()
    );
  }

  private String getWhere(boolean includeRange) {
    final StringJoiner whereJoiner = new StringJoiner(" AND ");

    condition.ifPresent(c -> whereJoiner.add(c.getAsWhereCondition(getTablePrefix())));
    if (includeRange) {
      after.ifPresent(__ -> whereJoiner.add(format("%s.id GT :after", getTablePrefix())));
      before.ifPresent(__ -> whereJoiner.add(format("%s.id LT :before", getTablePrefix())));
    }

    String where = whereJoiner.toString();

    if (where.isEmpty()) {
      return "";
    }

    if (includeRange) {
      if (customSqlOrder.getDirection().isDesc()) {
        where = where.replaceAll("LT", ">=")
          .replaceAll("GT", "<=");
      } else {
        where = where.replaceAll("LT", "<=")
          .replaceAll("GT", ">=");
      }
    }
    return "WHERE " + where;
  }

  private String addReverseOrder(String dataQuery) {
    if (!first) {
      var sql = format("""
            WITH __data__ AS (%s),
                 __ordered__ AS (SELECT * FROM __data__ ORDER BY row_number() OVER (PARTITION BY 1) DESC)
                 
            SELECT * FROM __ordered__
        """, dataQuery);

      return sql;
    }

    return dataQuery;
  }

  private String addCountQuery(String dataQuery) {
    var sql = format("""
          WITH __data__ AS (%s)
               
          SELECT count(*) as totalCount FROM __data__
      """, dataQuery);

    return sql;
  }

  protected String getOrderBy() {
    String orderBy = format("ORDER by %s, %s",
      customSqlOrder.toSql(getTablePrefix(), !first),
      getSqlOrderByIdAsc().toSql(getTablePrefix(), false));

    return orderBy;
  }

  protected String getTableName() {
    return tableName;
  }

  protected Class<?> getEntityType() {
    return entityType;
  }

  protected String getTablePrefix() {
    return "s";
  }

  protected SqlOrder getSqlOrderByIdAsc() {
    return new SqlOrder("id", OrderDirection.asc);
  }

}
