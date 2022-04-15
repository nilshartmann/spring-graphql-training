package nh.publy.backend.graphql.runtime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCoercing implements Coercing<LocalDateTime, String> {

  @Override
  public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
    // Convert a value returned by a DataFetcher to String
    if (!(dataFetcherResult instanceof LocalDateTime localDateTime)) {
      throw new CoercingSerializeException("DataFetcherResult must be instance of LocalDateTime");
    }

    return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
  }

  @Override
  public @NotNull LocalDateTime parseValue(@NotNull Object input) throws CoercingParseValueException {
    // Convert value from variable to LocalDateTime
    if (input instanceof String string) {
      return toLocalDateTime(string);
    }

    throw new CoercingSerializeException("Could not convert AST value " + input + " to DateTime");
  }

  @Override
  public @NotNull LocalDateTime parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
    // Convert AST value to LocalDateTime
    if (input instanceof StringValue string) {
      return toLocalDateTime(string.getValue());
    }

    throw new CoercingSerializeException("Could not convert value " + input + " to DateTime");
  }

  private static LocalDateTime toLocalDateTime(String value) {
    try {
      return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    } catch (Exception ex) {
      throw new CoercingSerializeException("Could not convert string to LocalDateTime: '" + value + "'");
    }
  }
}
