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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/stories-test-data.sql")
public class ReactionConnectionIntegrationTest {
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

  protected ReactionOrderBy customOrder = null;
  protected ReactionFilterCondition filterCondition = null;

  protected void withCondition(ReactionConditionField field, Object value) {
    filterCondition = new ReactionFilterCondition(field, value);
  }

  @BeforeEach
  void setupInputProjection() {
    project = new ConnectionTestUtils.DefaultInputProjection();
  }

  void orderByCreatedAtDesc() {
    this.customOrder = new ReactionOrder(OrderDirection.desc, ReactionOrderByField.createdAt);
  }

  void orderByCreatedAtAsc() {
    this.customOrder = new ReactionOrder(OrderDirection.asc, ReactionOrderByField.createdAt);
  }

  protected TestConnection expectTestConnection() {
    assertNotNull(entityManager);
    ReactionConnection reactionConnection = new ReactionConnection(
      entityManager,
      project,
      Optional.ofNullable(filterCondition),
      Optional.ofNullable(customOrder)
    );
    return new TestConnection(reactionConnection);
  }

  @Test
  public void asc_first_5() {
    project.first = 5;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void asc_last_5() {
    project.last = 5;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(2L, 3L, 4L, 5L, 6L).withTotalCount(6L);
  }

  @Test
  public void asc_last_2_withUser_U2() {
    project.last = 2;
    withCondition(ReactionConditionField.givenBy, 2);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(3L, 6L).withTotalCount(3L);
  }

  @Test
  public void asc_last_2_withStory_4() {
    project.last = 2;
    withCondition(ReactionConditionField.story, 4L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(5L, 6L).withTotalCount(3L);
  }

  static class ReactionOrder implements ReactionOrderBy {
    private final OrderDirection direction;
    private final ReactionOrderByField field;

    public ReactionOrder(OrderDirection direction, ReactionOrderByField field) {
      this.direction = direction;
      this.field = field;
    }

    public OrderDirection getDirection() {
      return direction;
    }

    @Override
    public ReactionOrderByField getField() {
      return field;
    }
  }
}