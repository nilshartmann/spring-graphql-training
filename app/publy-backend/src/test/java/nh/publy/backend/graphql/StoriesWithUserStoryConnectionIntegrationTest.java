package nh.publy.backend.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/stories-test-data.sql")
public class StoriesWithUserStoryConnectionIntegrationTest extends AbstractStoryConnectionIntegrationTest {
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

  void orderByCreatedAtDesc() {
    this.customOrder = new StoryOrder(OrderDirection.desc, StoryOrderByField.createdAt);
  }

  void orderByCreatedAtAsc() {
    this.customOrder = new StoryOrder(OrderDirection.asc, StoryOrderByField.createdAt);
  }

  @Test
  public void asc_first_5() {
    project.first = 5;
    withUserId = 15L;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 4L);
  }

  @Test
  public void asc_first_1() {
    project.first = 1;
    withUserId = 15L;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L);
  }

  @Test
  public void asc_last_1() {
    project.last = 1;
    withUserId = 15L;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(4L);
  }

  @Test
  public void desc_first_5() {
    project.first = 5;
    withUserId = 15L;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(4L, 1L).withTotalCount(2L);
  }

  @Test
  public void desc_first_1() {
    project.first = 1;
    withUserId = 15L;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(4L).withTotalCount(2L);
  }

  @Test
  public void desc_last_1() {
    project.last = 1;
    withUserId = 15L;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(1L).withTotalCount(2L);
  }
}