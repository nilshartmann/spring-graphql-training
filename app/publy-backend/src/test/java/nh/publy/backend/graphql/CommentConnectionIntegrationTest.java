package nh.publy.backend.graphql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/stories-test-data.sql")
public class CommentConnectionIntegrationTest {
  @Container
  static JdbcDatabaseContainer<?> database = new PostgreSQLContainer<>("postgres:14")
    .withDatabaseName("springboot")
    .withPassword("springboot")
    .withUsername("springboot")
    .withInitScript("schema.sql");

  @DynamicPropertySource
  static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
    propertyRegistry.add("spring.datasource.password", database::getPassword);
    propertyRegistry.add("spring.datasource.username", database::getUsername);
  }

  final Logger log = LoggerFactory.getLogger(getClass());

  @PersistenceContext
  EntityManager entityManager;

  protected ConnectionTestUtils.DefaultInputProjection project;

  protected CommentOrderBy customOrder = null;
  protected CommentFilterCondition filterCondition = null;

  protected void withCondition(CommentConditionField field, Object value) {
    filterCondition = new CommentFilterCondition(field, value);
  }

  @BeforeEach
  void setupInputProjection() {
    project = new ConnectionTestUtils.DefaultInputProjection();
  }

  protected TestConnection expectedTestConnection() {
    CommentConnection commentConnection = new CommentConnection(
      entityManager,
      project,
      Optional.ofNullable(filterCondition),
      Optional.ofNullable(customOrder)
    );
    return new TestConnection(commentConnection);
  }

  void orderByCreatedAtDesc() {
    this.customOrder = new CommentOrder(OrderDirection.desc, CommentOrderByField.createdAt);
  }

  void orderByCreatedAtAsc() {
    this.customOrder = new CommentOrder(OrderDirection.asc, CommentOrderByField.createdAt);
  }

  @Test
  public void asc_first_5() {
    project.first = 5;
    orderByCreatedAtAsc();
    expectedTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L).withTotalCount(8L);
  }

  @Test
  public void asc_last_5() {
    project.last = 5;
    orderByCreatedAtAsc();
    expectedTestConnection().withNodeIds(4L, 5L, 6L, 7L, 8L).withTotalCount(8L);
  }

  @Test
  public void desc_first_3() {
    project.first = 3;
    orderByCreatedAtDesc();
    expectedTestConnection().withNodeIds(8L, 7L, 6L).withTotalCount(8L);
  }

  @Test
  public void desc_first_4_story_1() {
    project.first = 4;
    withCondition(CommentConditionField.story, 1L);
    orderByCreatedAtDesc();
    expectedTestConnection().withNodeIds(7L, 5L, 4L, 2L).withTotalCount(5L);
  }

  @Test
  public void desc_first_3_user_1() {
    project.first = 3;
    withCondition(CommentConditionField.writtenBy, 1);
    orderByCreatedAtDesc();
    expectedTestConnection().withNodeIds(8L, 7L, 3L).withTotalCount(4L);
  }

  static class CommentOrder implements CommentOrderBy {
    private final OrderDirection direction;
    private final CommentOrderByField field;

    public CommentOrder(OrderDirection direction, CommentOrderByField field) {
      this.direction = direction;
      this.field = field;
    }

    public OrderDirection getDirection() {
      return direction;
    }

    @Override
    public CommentOrderByField getField() {
      return field;
    }
  }
}