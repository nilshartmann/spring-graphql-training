package nh.publy.backend.graphql;

import graphql.schema.idl.RuntimeWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;

@Component
public class PublyWiringConfigurer implements RuntimeWiringConfigurer {

  private static final Logger log = LoggerFactory.getLogger(PublyWiringConfigurer.class);

  @Override
  public void configure(RuntimeWiring.Builder builder) {
//    log.info(">>>>>>>>>>>>>>>> CONFIGURE {}", builder);
//    builder
//      .type("AddCommentPayload", typeBuilder -> typeBuilder.typeResolver(env -> {
//        Object javaObject = env.getObject();
//        if (javaObject instanceof AddCommentSuccessPayload) {
//          return env.getSchema().getObjectType("AddCommentSuccessPayload");
//        }
//        return env.getSchema().getObjectType("AddCommentFailurePayload");
//      }));
  }

}
