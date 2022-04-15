package nh.publy.backend.graphql;

import nh.publy.backend.domain.Story;
import nh.publy.backend.graphql.sql.SelectCountSqlBuilder;
import nh.publy.backend.graphql.sql.SelectNodesSqlBuilder;
import nh.publy.backend.graphql.sql.SqlOrder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static java.lang.String.*;

public class StoryConnection extends AbstractPublyConnection<Story> {

  private static final StoryOrderBy defaultStoryOrder = new StoryOrderBy() {
    @Override
    public OrderDirection getDirection() {
      return OrderDirection.asc;
    }

    @Override
    public StoryOrderByField getField() {
      return StoryOrderByField.natural;
    }
  };

  private final EntityManager entityManager;

  private final Optional<StoryOrderBy> orderBy;
  private final Optional<StoryFilterCondition> condition;

  public StoryConnection(EntityManager entityManager, PaginationInput paginationInput, Optional<StoryFilterCondition> condition, Optional<StoryOrderBy> orderBy) {
    super(paginationInput);
    this.entityManager = entityManager;
    this.orderBy = orderBy;
    this.condition = condition;
  }

  @Override
  protected List<Story> loadData(boolean fromFirst, Optional<Long> after, Optional<Long> before, int limit) {

    final StoryOrderBy storyOrderBy = this.orderBy.orElse(defaultStoryOrder);

    boolean external = storyOrderBy.getField() == StoryOrderByField.reactions;

    SqlOrder customSqlOrder = new SqlOrder(storyOrderBy.getField().getSqlFieldName(), external, storyOrderBy.getDirection());

    SelectNodesSqlBuilder builder = new SelectNodesSqlBuilder("stories", Story.class, customSqlOrder, this.condition, fromFirst, after, before, limit) {
      @Override
      protected String getSelectFrom() {
        if (storyOrderBy.getField() != StoryOrderByField.reactions) {
          return super.getSelectFrom();
        }

        final var tablePrefix = getTablePrefix();

        return format("SELECT %s.*, (select count (*) from reactions where reactions.story_id = %s.id) c FROM %s %s", tablePrefix, tablePrefix, getTableName(), tablePrefix);
      }
    };

    Query nativeQuery = builder.buildDataQuery(entityManager);
    List<Story> resultList = (List<Story>) nativeQuery.getResultList();
    return resultList;
  }

  @Override
  protected BigInteger loadTotalCount() {
    Query countQuery = new SelectCountSqlBuilder("stories", this.condition).buildCountQuery(entityManager);
    return (BigInteger) countQuery.getSingleResult();
  }
}
