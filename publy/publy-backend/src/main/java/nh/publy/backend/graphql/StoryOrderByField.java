package nh.publy.backend.graphql;

public enum StoryOrderByField {
  createdAt("created_at"),
  natural("id"),
  reactions("c");

  private final String sqlFieldName;

  StoryOrderByField(String sqlFieldName) {
    this.sqlFieldName = sqlFieldName;
  }

  public String getSqlFieldName() {
    return sqlFieldName;
  }


}
