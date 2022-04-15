package nh.publy.backend.graphql;

import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;

@Component
public class PublyRuntimeWiringConfigurer implements RuntimeWiringConfigurer {

  @Override
  public void configure(RuntimeWiring.Builder builder) {
    builder.type("AddCommentPayload", typeBuilder -> {
      return typeBuilder.typeResolver(new TypeResolver() {
        @Override
        public GraphQLObjectType getType(TypeResolutionEnvironment env) {
          Object object = env.getObject();
          
          if (object instanceof AddCommentSuccessResult) {
            return env.getSchema().getObjectType("AddCommentSuccessPayload");
          }

          return env.getSchema().getObjectType("AddCommentFailedPayload");
        }
      });
    });

    builder.type("OnNewFeedbackEvent", typeBuilder -> {
      return typeBuilder.typeResolver(new TypeResolver() {
        @Override
        public GraphQLObjectType getType(TypeResolutionEnvironment env) {
          Object object = env.getObject();

          if (object instanceof FeedbackReceivedEvent feedbackReceivedEvent) {
            NodeType feedbackNodeType = feedbackReceivedEvent.feedbackNodeId().getNodeType();
            return switch (feedbackNodeType) {
              case comment -> env.getSchema().getObjectType("OnNewCommentEvent");
              case reaction -> env.getSchema().getObjectType("OnNewReactionEvent");
              default -> null;
            };
          }
          return null;
        }
      });
    });
  }
}
