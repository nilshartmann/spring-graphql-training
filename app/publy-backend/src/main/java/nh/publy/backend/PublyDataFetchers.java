package nh.publy.backend;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class PublyDataFetchers {

  public DataFetcher<String> pingFetcher = new DataFetcher() {
    @Override
    public String get(DataFetchingEnvironment env) throws Exception {
      String msg = env.getArgumentOrDefault("msg", "Pong!");
      return msg;
    }
  };
}