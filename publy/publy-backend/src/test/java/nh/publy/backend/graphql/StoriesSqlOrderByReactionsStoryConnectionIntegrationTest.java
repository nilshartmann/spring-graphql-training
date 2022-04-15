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
public class StoriesSqlOrderByReactionsStoryConnectionIntegrationTest extends AbstractStoryConnectionIntegrationTest {
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

  void orderByReactionsDesc() {
    this.customOrder = new StoryOrder(OrderDirection.desc, StoryOrderByField.reactions);
  }

  void orderByReactionsAsc() {
    this.customOrder = new StoryOrder(OrderDirection.asc, StoryOrderByField.reactions);
  }

  @Test
  public void asc_first_5() {
  /*
   {
  allStories(first:5, orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }

   */
    project.first = 5;
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(1L, 5L, 3L, 2L, 4L).withTotalCount(5L);
  }

  @Test
  public void asc_first_2() {
   /*
   {
  allStories(first:2, orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }

   */
    project.first = 2;
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(1L, 5L);
  }

  @Test
  public void asc_first_2_after_3() {

    /*
    {
  allStoriesDesc: allStories(first:5, after:"WyJyY19hc2MiLFsyLDJdXQ==", orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }

     */

    project.first = 2;
    project.after = after(3L);
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(4L);
  }

  //
  @Test
  public void asc_first_2_before_2() {
    /*
   {
  allStoriesDesc: allStories (first:2, before:"WyJyY19hc2MiLFsyLDJdXQ==", orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }
     */
    project.first = 2;
    project.before = before(2L);
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(1L).withTotalCount(5L);
  }


  @Test
  public void desc_first_5() {
  /*
  query {
      allStories(first:5, orderBy:RC_DESC) {
        edges { cursor  node { id title  createdAt  } } } }   */
    project.first = 5;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(4L, 2L, 3L, 1L, 5L);
  }

  //
  @Test
  public void desc_first_2() {
  /*
 query {
      allStories(first:2, orderBy:RC_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.first = 2;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(4L, 2L);
  }

  // ---------------------------------- LAST -------------------------------------------------------------------------

  @Test
  public void asc_last_5() {
  /*
   {
  allStories(last:5, orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }

   */
    project.last = 5;
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(5L, 1L, 3L, 2L, 4L);
  }


  @Test
  public void asc_last_2() {
   /*
   {  allStories(last:2, orderBy:RC_ASC) { edges {cursor node { id title rc } } } }
   */
    project.last = 2;
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(2L, 4L);
  }

  @Test
  public void asc_last_2_after_3() {
    /*
 {
  allStories(last:2 after: "WyJyY19hc2MiLFsxLDNdXQ==" orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }
     */

    project.last = 2;
    project.after = after(3L);
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(4L);
  }

  @Test
  public void asc_last_2_before_2() {
    /*
   {
  allStoriesDesc: allStories (first:2, before:"WyJyY19hc2MiLFsyLDJdXQ==", orderBy:RC_ASC) {
    edges {cursor node { id title rc } } } }
     */
    project.last = 2;
    project.before = before(2L);
    orderByReactionsAsc();
    expectTestConnection().withNodeIds(1L);
  }


  @Test
  public void desc_last_5() {
  /*
  query {
      allStories(first:5, orderBy:RC_DESC) {
        edges { cursor  node { id title  createdAt  } } } }   */
    project.last = 5;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(4L, 2L, 3L, 5L, 1L);
  }

  //
  @Test
  public void desc_last_2() {
  /*
 query {
      allStories(first:2, orderBy:RC_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.last = 2;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(5L, 1L);
  }

  @Test
  public void desc_last_2_for_user_15() {
    project.last = 2;
    withUserId = 15L;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(4L, 1L).withTotalCount(2);
  }

  @Test
  public void desc_first_1_for_user_15() {
    project.first = 1;
    withUserId = 15L;
    orderByReactionsDesc();
    expectTestConnection().withNodeIds(4L).withTotalCount(2);
  }
}