package nh.publy.backend.graphql;

public enum OrderDirection {

  asc,
  desc;

  public boolean isAsc() {
    return this == asc;
  }

  public boolean isDesc() {
    return this == desc;
  }
}
