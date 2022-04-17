package nh.publy.backend.graphql;

import graphql.relay.Edge;
import nh.publy.backend.domain.Identifiable;

import java.math.BigInteger;
import java.util.List;

import static java.lang.String.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestConnection {
  private final AbstractPublyConnection<?> actualConnection;

  public TestConnection(AbstractPublyConnection<?> actualConnection) {
    this.actualConnection = actualConnection;
  }

  public TestConnection withTotalCount(long expectedCount) {
    assertNotNull(this.actualConnection.getTotalCount());
    assertEquals(BigInteger.valueOf(expectedCount), this.actualConnection.getTotalCount());
    return this;
  }

  TestConnection withNodeIds(Long... expectedNodeIds) {
    List<? extends Edge<? extends Identifiable>> edges = actualConnection.getEdges();
    assertNotNull(edges);

    assertEquals(expectedNodeIds.length, edges.size(), format("Expected '%s' nodes, but got: '%s'", expectedNodeIds.length, edges.size()));
    for (int i = 0; i < expectedNodeIds.length; i++) {
      long expectedNodeId = expectedNodeIds[i];

      Long actual = edges.get(i).getNode().getId();
      assertEquals(expectedNodeId, actual, format("In Row %d expected nodeId '%s' but got: '%s'", i, expectedNodeId, actual));
    }
    return this;
  }
}
