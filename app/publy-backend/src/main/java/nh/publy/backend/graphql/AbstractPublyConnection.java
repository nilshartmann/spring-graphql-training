package nh.publy.backend.graphql;


import graphql.relay.*;
import nh.publy.backend.domain.Identifiable;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPublyConnection<T extends Identifiable> implements Connection<T> {

  private final PaginationInput paginationInput;
  private ConnectionData<T> connectionData;
  private BigInteger totalCount;

  public AbstractPublyConnection(PaginationInput paginationInput) {
    this.paginationInput = paginationInput;
  }

  @Override
  public List<Edge<T>> getEdges() {
    return getConnectionData().edges();
  }

  @Override
  public PageInfo getPageInfo() {
    return getConnectionData().pageInfo();
  }

  public Iterator<T> getNodes() {
    return getConnectionData().nodes();
  }

  public BigInteger getTotalCount() {
    if (this.totalCount == null) {
      this.totalCount = loadTotalCount();
    }

    return this.totalCount;
  }

  protected abstract List<T> loadData(boolean fromFirst, Optional<Long> after, Optional<Long> before, int limit);

  protected abstract BigInteger loadTotalCount();

  protected ConnectionData<T> getConnectionData() {
    if (this.connectionData == null) {
      this.connectionData = createConnectionData();
    }

    return this.connectionData;
  }

  /**
   * @link graphql.relay.SimpleListConnection
   * @see <a href="https://stackoverflow.com/a/66300422">How to implement GraphQL cursor in SQL query with sort by</a>
   */
  protected ConnectionData<T> createConnectionData() {

    final Integer first = paginationInput.getFirst().orElse(null);
    final Integer last = paginationInput.getLast().orElse(null);

    if (first != null && last != null) {
      throw new InvalidPaginationDataException("Please specify EITHER 'first' or 'last', but not BOTH");
    }

    if (first == null && last == null) {
      throw new InvalidPaginationDataException("Please specify either 'first' or 'last'");
    }


    Optional<String> after = paginationInput.getAfter();
    Optional<String> before = paginationInput.getBefore();

    var allEdges = buildEdges(first, last, after, before);

    if (allEdges.isEmpty()) {
      return emptyConnectionData();
    }

    ConnectionCursor firstPresliceCursor = allEdges.get(0).getCursor();
    ConnectionCursor lastPresliceCursor = allEdges.get(allEdges.size() - 1).getCursor();

    List<DefaultEdge<T>> edges = applyCursorsToEdges(after, before, allEdges);

    if (first != null) {
      edges = edges.subList(0, first <= edges.size() ? first : edges.size());
    }
    if (last != null) {
      edges = edges.subList(last > edges.size() ? 0 : edges.size() - last, edges.size());
    }

    if (edges.isEmpty()) {
      return emptyConnectionData();
    }

    Edge<T> firstEdge = edges.get(0);
    Edge<T> lastEdge = edges.get(edges.size() - 1);

    PageInfo pageInfo = new DefaultPageInfo(
      firstEdge.getCursor(),
      lastEdge.getCursor(),
      !firstEdge.getCursor().equals(firstPresliceCursor),
      !lastEdge.getCursor().equals(lastPresliceCursor)
    );
    var connection = new ConnectionData<>(Collections.unmodifiableList(edges), pageInfo);
    return connection;
  }

  private List<DefaultEdge<T>> buildEdges(Integer first, Integer last, Optional<String> after, Optional<String> before) {
    int itemsToFetchCount;

    if (first != null) {
      itemsToFetchCount = first;
      if (itemsToFetchCount < 0) {
        throw new InvalidPaginationDataException("Please set 'first' to a value greater than 0");
      }
      if (itemsToFetchCount > 100) {
        throw new InvalidPaginationDataException("Please set 'first' to a value of at most 100");
      }
    } else {
      itemsToFetchCount = last;

      if (itemsToFetchCount < 0) {
        throw new InvalidPaginationDataException("Please set 'last' to a value greater than 0");
      }
      if (itemsToFetchCount > 100) {
        throw new InvalidPaginationDataException("Please set 'last' to a value of at most 100");
      }
    }

    var result = loadData(
      first != null,
      after.map(PublyCursor::decode),
      before.map(PublyCursor::decode),
      itemsToFetchCount + 2
    );

    if (result.isEmpty()) {
      return Collections.emptyList();
    }

    List<DefaultEdge<T>> allEdges = asEdgeList(result);
    return allEdges;
  }

  private ConnectionData<T> emptyConnectionData() {
    return new ConnectionData<>(Collections.emptyList(), new DefaultPageInfo(null, null, false, false));
  }

  /**
   * @see <a href="https://relay.dev/graphql/connections.htm#ApplyCursorsToEdges()">ApplyCursorsToEdges</a>
   */
  private List<DefaultEdge<T>> applyCursorsToEdges(final Optional<String> after, final Optional<String> before, final List<DefaultEdge<T>> edges) {
    int beginIndex = 0;
    String afterCursor = after.orElse(null);
    if (afterCursor != null) {
      for (int i = 0; i < edges.size(); i++) {
        DefaultEdge<T> t = edges.get(i);
        if (t.getCursor().getValue().equals(afterCursor)) {
          beginIndex = i + 1;
          break;
        }
      }
    }

    int endIndex = edges.size();
    String beforeCursor = before.orElse(null);
    if (beforeCursor != null) {
      for (int i = 0; i < edges.size(); i++) {
        DefaultEdge<T> t = edges.get(i);
        if (t.getCursor().getValue().equals(beforeCursor)) {
          endIndex = i;
          break;
        }
      }
    }

    if (beginIndex > endIndex) beginIndex = endIndex;

    if (beginIndex == 0 && endIndex == edges.size()) {
      return edges;
    }

    return edges.subList(beginIndex, endIndex);
  }

  private List<DefaultEdge<T>> asEdgeList(List<T> result) {
    List<DefaultEdge<T>> edges = result.stream().map(
      node -> new DefaultEdge<>(node, PublyCursor.forObject(node))
    ).toList();
    return edges;
  }

  record ConnectionData<T>(List<Edge<T>> edges, PageInfo pageInfo) {
    public Iterator<T> nodes() {
      final Iterator<Edge<T>> edgeIterator = edges.iterator();
      return new Iterator<T>() {
        @Override
        public boolean hasNext() {
          return edgeIterator.hasNext();
        }

        @Override
        public T next() {
          Edge<T> edge = edgeIterator.next();
          return edge.getNode();
        }
      };
    }

  }

  ;

}
