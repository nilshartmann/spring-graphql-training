package nh.publy.backend.graphql;

import graphql.relay.DefaultConnectionCursor;
import nh.publy.backend.domain.Identifiable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.LongStream;

public class ConnectionTestUtils {

  public static SimpleIdentifiable node(Long id) {
    return new SimpleIdentifiable(id);
  }

  public static String before(Long value) {
    return PublyCursor.forObject(new SimpleIdentifiable(value)).getValue();
  }

  public static String after(Long value) {
    return PublyCursor.forObject(new SimpleIdentifiable(value)).getValue();
  }

  public static DefaultConnectionCursor cursor(Long value) {
    return PublyCursor.forObject(new SimpleIdentifiable(value));
  }

  public static List<SimpleIdentifiable> numberedList(int min, int max) {
    return LongStream.range(min, max + 1).boxed().map(SimpleIdentifiable::new).toList();
  }

  public static class DefaultInputProjection implements StoriesInput {

    Integer first;
    String after;
    Integer last;
    String before;

    @Override
    public Optional<Integer> getFirst() {
      return Optional.ofNullable(first);
    }

    @Override
    public Optional<String> getAfter() {
      return Optional.ofNullable(after);
    }

    @Override
    public Optional<Integer> getLast() {
      return Optional.ofNullable(last);
    }

    @Override
    public Optional<String> getBefore() {
      return Optional.ofNullable(before);
    }

    @Override
    public Optional<StoryOrderBy> getOrderBy() {
      return Optional.ofNullable(null);
    }

  }

  public static class SimpleIdentifiable implements Identifiable {

    private final Long id;

    SimpleIdentifiable(Long id) {
      this.id = id;
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SimpleIdentifiable that = (SimpleIdentifiable) o;
      return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }

    public String toString() {
      return "[SimpleIdentifiable, id: " + this.id + "(" + PublyCursor.forId(this.getId()) + ")]";
    }
  }
}
