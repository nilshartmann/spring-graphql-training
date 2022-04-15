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

import static nh.publy.backend.graphql.ConnectionTestUtils.after;
import static nh.publy.backend.graphql.ConnectionTestUtils.before;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/stories-test-data.sql")
public class StoriesSqlOrderByIdStoryConnectionIntegrationTest extends AbstractStoryConnectionIntegrationTest {
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

  @Test
  public void first_5() {
    project.first = 5;
    expectTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void first_2() {
    project.first = 2;
    expectTestConnection().withNodeIds(1L, 2L);
  }

  @Test
  public void first_2_after_2() {
    project.first = 2;
    project.after = after(2L);
    expectTestConnection().withNodeIds(3L, 4L);
  }

  //
  @Test
  public void first_2_before_4() {
    project.first = 2;
    project.before = before(4L);
    expectTestConnection().withNodeIds(1L, 2L);
  }

  @Test
  public void first_3_after_1_before_4() {
    project.first = 3;
    project.after = after(1L);
    project.before = before(4L);
    expectTestConnection().withNodeIds(2L, 3L);
  }

  // ------------------------------- LAST --------------------------------------------------------------------------
  @Test
  public void last_5() {
    project.last = 5;
    expectTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void last_2() {
    project.last = 2;
    expectTestConnection().withNodeIds(4L, 5L);
  }

  @Test
  public void last_2_after_2() {
    project.last = 2;
    project.after = after(2L);
    expectTestConnection().withNodeIds(4L, 5L);
  }

  //
  @Test
  public void last_2_before_4() {
    project.last = 2;
    project.before = before(4L);
    expectTestConnection().withNodeIds(2L, 3L);
  }

  /*

   */
  @Test
  public void last_4_after_1_before_5() {
    project.last = 4;
    project.after = after(1L);
    project.before = before(5L);
    expectTestConnection().withNodeIds(2L, 3L, 4L);
  }


}