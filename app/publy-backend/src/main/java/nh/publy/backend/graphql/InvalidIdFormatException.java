package nh.publy.backend.graphql;

import static java.lang.String.*;

public class InvalidIdFormatException extends RuntimeException {

  private final String id;

  public InvalidIdFormatException(String id) {
    super(format("Specified id '%s' has invalid format", id));
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
