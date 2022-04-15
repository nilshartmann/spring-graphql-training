package nh.publy.backend.graphql;

import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;

@Component
public class DateTimeRuntimeWiringConfigurer implements RuntimeWiringConfigurer {
  @Override
  public void configure(RuntimeWiring.Builder builder) {
    builder.scalar(GraphQLScalarType.newScalar()
      .name("DateTime").coercing(new DateTimeCoercing())
      .build());
  }
}
