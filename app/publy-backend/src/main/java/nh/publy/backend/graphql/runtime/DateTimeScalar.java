package nh.publy.backend.graphql.runtime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeScalar {

  public static final GraphQLScalarType DateTimeScalarType = GraphQLScalarType.newScalar()
    .name("DateTime")
    .description("Date send is written to/parsed from ISO 8601 format")
    .coercing(new Coercing() {
      @Override
      public Object serialize(Object dataFetcherResult) {

        // Convert a value returned by a DataFetcher to String
        if (!(dataFetcherResult instanceof LocalDateTime localDateTime)) {
          throw new CoercingSerializeException("DataFetcherResult must be instance of LocalDateTime");
        }

        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
      }

      @Override
      public Object parseValue(Object input) {
        if (input instanceof String string) {
          return toLocalDateTime(string);
        }

        throw new CoercingSerializeException("Could not convert AST value " + input + " to DateTime");
      }

      @Override
      public Object parseLiteral(Object input) {
        if (input instanceof StringValue string) {
          return toLocalDateTime(string.getValue());
        }

        throw new CoercingSerializeException("Could not convert value " + input + " to DateTime");
      }
    })
    .build();

  private static LocalDateTime toLocalDateTime(String value) {
    try {
      return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    } catch (Exception ex) {
      throw new CoercingSerializeException("Could not convert string to LocalDateTime: '" + value + "'");
    }
  }

}
