package nh.publy.backend.graphql.sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

import static java.lang.String.*;

public class SelectCountSqlBuilder {
  private final Optional<? extends SqlFilterCondition> condition;
  private final String tableName;

  public SelectCountSqlBuilder(String tableName, Optional<? extends SqlFilterCondition> condition) {
    this.tableName = tableName;
    this.condition = condition;
  }

  public Query buildCountQuery(EntityManager entityManager) {
    String selectFrom = getSelectFrom();
    String where = getWhere();

    String sqlData = format("%s %s",
      selectFrom,
      where);

    String sql = addCountQuery(sqlData);

    Query nativeQuery = entityManager.createNativeQuery(sql);
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

  private String getWhere() {
    return condition.map(c -> "WHERE " + c.getAsWhereCondition(getTablePrefix())).orElse("");
  }

  private String addCountQuery(String dataQuery) {
    var sql = format("""
          WITH __data__ AS (%s)
               
          SELECT count(*) as totalCount FROM __data__
      """, dataQuery);

    return sql;
  }

  protected String getTablePrefix() {
    return "s";
  }

  protected String getTableName() {
    return this.tableName;
  }
}
