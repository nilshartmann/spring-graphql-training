package nh.publy.backend.graphql;

public enum ReactionOrderByField {
  createdAt("created_at"),
  natural("id");

  private final String sqlFieldName;

  ReactionOrderByField(String sqlFieldName) {
    this.sqlFieldName = sqlFieldName;
  }

  public String getSqlFieldName() {
    return sqlFieldName;
  }
}
