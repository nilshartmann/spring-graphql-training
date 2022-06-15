package nh.publy.backend.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PublyGraphQLController {
  @QueryMapping
  public String ping() {
    return "Pong!";
  }
}
