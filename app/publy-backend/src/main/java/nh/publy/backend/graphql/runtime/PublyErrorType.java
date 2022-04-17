package nh.publy.backend.graphql.runtime;

import graphql.ErrorClassification;

public enum PublyErrorType implements ErrorClassification {

  InvalidPaginationBoundaries,
  invalidNodeId
}
