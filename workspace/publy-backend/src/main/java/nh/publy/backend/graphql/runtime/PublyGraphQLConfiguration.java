package nh.publy.backend.graphql.runtime;

import graphql.analysis.FieldComplexityCalculator;
import graphql.analysis.FieldComplexityEnvironment;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.language.IntValue;
import graphql.schema.GraphQLDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublyGraphQLConfiguration {

  @Bean
  public MaxQueryDepthInstrumentation maxQueryDepthInstrumentation() {
    return new MaxQueryDepthInstrumentation(15);
  }

  @Bean
  public MaxQueryComplexityInstrumentation maxQueryComplexityInstrumentation() {
    return new MaxQueryComplexityInstrumentation(60, new PublyFieldComplexityCalculator());
  }

  class PublyFieldComplexityCalculator implements FieldComplexityCalculator {
    @Override
    public int calculate(FieldComplexityEnvironment environment, int childComplexity) {
      GraphQLDirective complexity = environment.getFieldDefinition().getDirective("Complexity");
      if (complexity != null) {
        IntValue value = (IntValue) complexity
          .getArgument("complexity")
          .getArgumentValue()
          .getValue();

        return value.getValue().intValue() + childComplexity;
      }
      return 1 + childComplexity;
    }
  }

}
