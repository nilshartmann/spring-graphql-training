package nh.publy.backend.graphql.runtime;

import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class PublyGraphQLConfig {

  // NOTE: ------------------------------------------------------
  //       Instrumentations also effect the introspection query!
  //       Keep that in mind, when limiting query depth or
  //         complexity
  //       ------------------------------------------------------

//  @Bean
//  public TracingInstrumentation tracingInstrumentation() {
//    return new TracingInstrumentation(TracingInstrumentation.Options.newOptions().includeTrivialDataFetchers(false));
//  }

  @Bean
  public RuntimeWiringConfigurer scalarConfigurer() {
    return builder -> builder.scalar(GraphQLScalarType.newScalar()
      .name("DateTime")
      .coercing(new DateTimeCoercing())
      .build()
    );
  }

  @Bean
  public MaxQueryDepthInstrumentation maxQueryDepthInstrumentation() {
    return new MaxQueryDepthInstrumentation(20);
  }

//  @Bean
//  public MaxQueryComplexityInstrumentation maxQueryComplexityInstrumentation() {
//    return new MaxQueryComplexityInstrumentation(10);
//  }

//  @Bean
//  public PublyTracingInstrumentation publyTracingInstrumentation() {
//    return new PublyTracingInstrumentation();
//  }


}
