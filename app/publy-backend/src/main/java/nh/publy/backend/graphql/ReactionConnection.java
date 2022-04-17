package nh.publy.backend.graphql;

import nh.publy.backend.domain.Reaction;
import nh.publy.backend.graphql.sql.SelectCountSqlBuilder;
import nh.publy.backend.graphql.sql.SelectNodesSqlBuilder;
import nh.publy.backend.graphql.sql.SqlOrder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class ReactionConnection extends AbstractPublyConnection<Reaction> {
  private static final ReactionOrderBy defaultReactionOrder = new ReactionOrderBy() {
    @Override
    public OrderDirection getDirection() {
      return OrderDirection.asc;
    }

    @Override
    public ReactionOrderByField getField() {
      return ReactionOrderByField.natural;
    }
  };


  private final EntityManager entityManager;

  private final Optional<ReactionOrderBy> orderBy;
  private final Optional<ReactionFilterCondition> condition;

  public ReactionConnection(EntityManager entityManager, PaginationInput paginationInput, Optional<ReactionFilterCondition> condition, Optional<ReactionOrderBy> orderBy) {
    super(paginationInput);
    this.entityManager = entityManager;
    this.orderBy = orderBy;
    this.condition = condition;
  }

  @Override
  protected List<Reaction> loadData(boolean fromFirst, Optional<Long> after, Optional<Long> before, int limit) {
    ReactionOrderBy reactionOrderBy = this.orderBy.orElse(defaultReactionOrder);
    SqlOrder customSqlOrder = new SqlOrder(reactionOrderBy.getField().getSqlFieldName(), reactionOrderBy.getDirection());

    SelectNodesSqlBuilder builder = new SelectNodesSqlBuilder(
      "reactions",
      Reaction.class,
      customSqlOrder,
      this.condition,
      fromFirst,
      after,
      before,
      limit
    );

    Query nativeQuery = builder.buildDataQuery(entityManager);
    List<Reaction> resultList = (List<Reaction>) nativeQuery.getResultList();
    return resultList;
  }

  @Override
  protected BigInteger loadTotalCount() {
    Query countQuery = new SelectCountSqlBuilder("reactions", condition)
      .buildCountQuery(entityManager);
    return (BigInteger) countQuery.getSingleResult();
  }

}
