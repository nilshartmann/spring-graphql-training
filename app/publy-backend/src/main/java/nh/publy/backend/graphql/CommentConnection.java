package nh.publy.backend.graphql;

import nh.publy.backend.domain.Comment;
import nh.publy.backend.graphql.sql.SelectCountSqlBuilder;
import nh.publy.backend.graphql.sql.SelectNodesSqlBuilder;
import nh.publy.backend.graphql.sql.SqlOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class CommentConnection extends AbstractPublyConnection<Comment> {
  private final static CommentOrderBy defaultCommentOrder = new CommentOrderBy() {
    @Override
    public OrderDirection getDirection() {
      return OrderDirection.asc;
    }

    @Override
    public CommentOrderByField getField() {
      return CommentOrderByField.natural;
    }
  };

  @PersistenceContext
  private EntityManager entityManager;
  private final Optional<CommentOrderBy> orderBy;
  private final Optional<CommentFilterCondition> condition;

  public CommentConnection(EntityManager entityManager, PaginationInput paginationInput, Optional<CommentFilterCondition> condition, Optional<CommentOrderBy> orderBy) {
    super(paginationInput);
    this.entityManager = entityManager;
    this.orderBy = orderBy;
    this.condition = condition;
  }

  @Override
  protected List<Comment> loadData(boolean fromFirst, Optional<Long> after, Optional<Long> before, int limit) {
    CommentOrderBy commentOrderBy = orderBy.orElse(defaultCommentOrder);

    SqlOrder customSqlOrder = new SqlOrder(commentOrderBy.getField().getSqlFieldName(), commentOrderBy.getDirection());

    SelectNodesSqlBuilder builder = new SelectNodesSqlBuilder(
      "comments",
      Comment.class,
      customSqlOrder,
      this.condition,
      fromFirst,
      after,
      before,
      limit
    );

    Query nativeQuery = builder.buildDataQuery(entityManager);
    List<Comment> resultList = (List<Comment>) nativeQuery.getResultList();
    return resultList;
  }

  @Override
  protected BigInteger loadTotalCount() {
    Query countQuery = new SelectCountSqlBuilder("comments", this.condition)
      .buildCountQuery(entityManager);
    return (BigInteger) countQuery.getSingleResult();

  }
}
