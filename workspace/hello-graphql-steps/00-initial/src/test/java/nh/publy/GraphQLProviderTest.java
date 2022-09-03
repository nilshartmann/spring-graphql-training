package nh.publy;

import nh.publy.graphql.GraphQLProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphQLProviderTest {

  @Test
  void graphQLProvider() {
    // Nur um sicherzustellen, dass alles compiliert
    GraphQLProvider graphQLProvider = new GraphQLProvider();
    assertThat(graphQLProvider).isNotNull();
  }

}
