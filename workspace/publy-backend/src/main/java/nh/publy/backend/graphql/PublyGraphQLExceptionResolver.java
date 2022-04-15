package nh.publy.backend.graphql;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

@Component
public class PublyGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

    if (ex instanceof BindException bindException) {
      FieldError fieldError = bindException.getFieldError();
      if (fieldError != null && fieldError.contains(Throwable.class)) {
        Throwable t = NestedExceptionUtils.getMostSpecificCause(fieldError.unwrap(Throwable.class));
        if (t instanceof InvalidIdFormatException ife) {
          return GraphqlErrorBuilder.newError(env)
            .message("Could not parse id " + ife.getId())
            .extensions(Map.of("id", ife.getId()))
            .errorType(PublyGraphQLErrorClassification.invalidNodeId)
            .build();
        }
      }
    }

    return super.resolveToSingleError(ex, env);
  }

  @Override
  protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {

    Throwable t = NestedExceptionUtils.getMostSpecificCause(ex);

    if (t instanceof ConstraintViolationException vex) {
      return vex.getConstraintViolations().stream().map(
        v -> GraphqlErrorBuilder.newError(env)
          .message(v.getMessage())
          .errorType(PublyGraphQLErrorClassification.constraintViolation)
          .extensions(
            Map.of("propertyName", v.getPropertyPath().toString())
          ).build()
      ).toList();
    }

    return super.resolveToMultipleErrors(ex, env);
  }

  static enum PublyGraphQLErrorClassification implements ErrorClassification {
    invalidNodeId,
    constraintViolation
  }
}
