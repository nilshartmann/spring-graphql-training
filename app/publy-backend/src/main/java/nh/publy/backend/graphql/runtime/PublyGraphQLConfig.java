package nh.publy.backend.graphql.runtime;

import graphql.analysis.FieldComplexityCalculator;
import graphql.analysis.FieldComplexityEnvironment;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.language.IntValue;
import graphql.schema.GraphQLDirective;
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

  // Enable only during workshops and demos!
//  @Bean
//  public MaxQueryComplexityInstrumentation maxQueryComplexityInstrumentation() {
//    return new MaxQueryComplexityInstrumentation(60, new PublyFieldComplexityCalculator());
//  }

  /**
   * Enhancement of a FieldComplexityCalculator that allows you to specify the
   * "complexity" of a field using a custom "Complexity" directive in your schema.
   *
   * This way you can make fields that require more backend resources when requested
   * more "complex", for example fields that needs additional database or
   * service calls.
   *
   * <code>
   *   type Pet {
   *     name: String! # Default Complexity: 1
   *     owner: PetOwner! @Complexity(complexity: 10)
   *   }
   * </code>
   */
  class PublyFieldComplexityCalculator implements FieldComplexityCalculator {
    @Override
    public int calculate(FieldComplexityEnvironment environment, int childComplexity) {
      GraphQLDirective complexity = environment.getFieldDefinition().getDirective("Complexity");
      if (complexity != null) {
        IntValue value = complexity
          .getArgument("complexity")
          .toAppliedArgument()
          .getValue();

        return value.getValue().intValue() + childComplexity;
      }
      return 1 + childComplexity;
    }
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
