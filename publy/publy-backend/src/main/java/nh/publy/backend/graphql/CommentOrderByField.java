package nh.publy.backend.graphql;

public enum CommentOrderByField {
  createdAt("created_at"),
  natural("id");

  private final String sqlFieldName;

  CommentOrderByField(String sqlFieldName) {
    this.sqlFieldName = sqlFieldName;
  }

  public String getSqlFieldName() {
    return sqlFieldName;
  }
}
