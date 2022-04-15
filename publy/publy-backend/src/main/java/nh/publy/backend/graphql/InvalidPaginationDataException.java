package nh.publy.backend.graphql;

public class InvalidPaginationDataException extends RuntimeException {

  public InvalidPaginationDataException(String msg) {
    super(msg);
  }

}
