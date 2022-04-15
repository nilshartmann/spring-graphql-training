package nh.publy.backend.graphql.runtime;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import nh.publy.backend.graphql.InvalidIdFormatException;
import nh.publy.backend.graphql.InvalidPaginationDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.Map;

@Component
public class PublyGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

  private static final Logger log = LoggerFactory.getLogger(PublyGraphQLExceptionResolver.class);

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    Throwable t = NestedExceptionUtils.getMostSpecificCause(ex);
    if (t instanceof InvalidPaginationDataException) {
      return GraphqlErrorBuilder.newError(env)
        .errorType(PublyErrorType.InvalidPaginationBoundaries)
        .message(t.getMessage())
        .build();
    }

    if (ex instanceof BindException bindException) {
      FieldError fieldError = bindException.getFieldError();
      if (fieldError != null && fieldError.contains(Throwable.class)) {
        Throwable fieldEx = NestedExceptionUtils.getMostSpecificCause(fieldError.unwrap(Throwable.class));
        if (fieldEx instanceof InvalidIdFormatException ife) {
          return GraphqlErrorBuilder.newError(env)
            .message("Could not parse id " + ife.getId())
            .extensions(Map.of("id", ife.getId()))
            .errorType(PublyErrorType.invalidNodeId)
            .build();
        }
      }
    }

    return null;
  }
}
