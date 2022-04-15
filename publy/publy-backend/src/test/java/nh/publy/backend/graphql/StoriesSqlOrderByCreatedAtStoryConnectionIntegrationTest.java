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
public class StoriesSqlOrderByCreatedAtStoryConnectionIntegrationTest extends AbstractStoryConnectionIntegrationTest {
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
  /*
    query {
      allStories(first:5, orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.first = 5;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void asc_first_2() {
  /*
    query {
      allStories(first:2, orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.first = 2;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 2L);
  }

  @Test
  public void asc_first_2_after_2() {
    /*
     query {
      allStories(first:2, after:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTAyVDIwOjA3OjM3LjkxNyJdXQ==",  orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.first = 2;
    project.after = after(2L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(3L, 4L);
  }

  //
  @Test
  public void asc_first_2_before_4() {
    /*
    query {
      allStories(first:2, before:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTA0VDIwOjA3OjM3LjkxNyJdXQ==",  orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.first = 2;
    project.before = before(4L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 2L);
  }

  @Test
  public void asc_first_3_after_1_before_4() {
    /*
    query {

      allStoriesDesc: allStories(first:3,after:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTAxVDIwOjA3OjM3LjkxNyIsMV1d", before:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTA0VDIwOjA3OjM3LjkxNyIsNF1d" orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } }
    }
     */
    project.first = 3;
    project.after = after(1L);
    project.before = before(4L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(2L, 3L);
  }

  @Test
  public void desc_first_5() {
  /*
    query {
      allStories(first:5, orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.first = 5;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(5L, 4L, 3L, 2L, 1L);
  }

  //
  @Test
  public void desc_first_2() {
  /*
 query {
      allStories(first:2, orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.first = 2;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(5L, 4L);
  }

  //
  @Test
  public void desc_first_2_after_3() {
    /*
     query {
      allStories(first:2, after: "WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wM1QyMDowNzozNy45MTciLDNdXQ==", orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.first = 2;
    project.after = after(3L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(2L, 1L);
  }

  @Test
  public void desc_first_2_before_3() {
    /*
     query {
       allStories(first:2, before: "WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wM1QyMDowNzozNy45MTciLDNdXQ==", orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } } }     */
    project.first = 2;
    project.before = before(3L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(5L, 4L);
  }


  @Test
  public void desc_first_3_after_4_before_1() {
    /*
  query {

      allStoriesDesc: allStories(
        first:3,
        after: "WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wNFQyMDowNzozNy45MTciLDRdXQ==",
        before:"WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wMVQyMDowNzozNy45MTciLDFdXQ=="
        orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } }
    }
     */
    project.first = 3;
    project.after = after(4L);
    project.before = before(1L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(3L, 2L);
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  @Test
  public void asc_last_5() {
  /*
    query {
      allStories(last:5, orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.last = 5;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void asc_last_2() {
  /*
    query {
      allStories(last:2, orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.last = 2;
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(4L, 5L);
  }

  @Test
  public void asc_last_2_after_2() {
    /*
         query {
      allStories(last:2, after: "WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTAyVDIwOjA3OjM3LjkxNyIsMl1d", orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.last = 2;
    project.after = after(2L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(4L, 5L);
  }

  //
  @Test
  public void asc_last_2_before_4() {
    /*
    query {
      allStories(last:2, before:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTA0VDIwOjA3OjM3LjkxNyIsNF1d" orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.last = 2;
    project.before = before(4L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(2L, 3L);
  }

  @Test
  public void asc_last_3_after_1_before_4() {
    /*
        query {
      allStories(last:3 after:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTAxVDIwOjA3OjM3LjkxNyIsMV1d" before:"WyJjcmVhdGVkX2F0X2FzYyIsWyIyMDIxLTEwLTA0VDIwOjA3OjM3LjkxNyIsNF1d" orderBy:CREATED_AT_ASC) {
        edges { cursor  node { id title  createdAt  } } } }
    }
     */
    project.last = 3;
    project.after = after(1L);
    project.before = before(4L);
    orderByCreatedAtAsc();
    expectTestConnection().withNodeIds(2L, 3L);
  }

  @Test
  public void desc_last_5() {
  /*
    query {
      allStories(last:5, orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.last = 5;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(5L, 4L, 3L, 2L, 1L);
  }

  //
  @Test
  public void desc_last_2() {
  /*
 query {
      allStories(last:2, orderBy:CREATED_AT_DESC) {
        edges { cursor  node { id title  createdAt  } } } }
   */
    project.last = 2;
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(2L, 1L);
  }

  //
  @Test
  public void desc_last_2_after_3() {
    /*
  query {
      allStories(last:2 after:"WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wM1QyMDowNzozNy45MTciLDNdXQ==" orderBy:CREATED_AT_DESC) {
      totalCount
      pageInfo { hasNextPage endCursor }
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.last = 2;
    project.after = after(3L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(2L, 1L);
  }

  @Test
  public void desc_last_2_before_3() {
    /*
  query {
      allStories(last:2 before:"WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wM1QyMDowNzozNy45MTciLDNdXQ==" orderBy:CREATED_AT_DESC) {
      totalCount
      pageInfo { hasNextPage endCursor }
        edges { cursor  node { id title  createdAt  } } } }
         */
    project.last = 2;
    project.before = before(3L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(5L, 4L);
  }


  @Test
  public void desc_last_4_after_5_before_1() {
    /*
   query {
      allStories(
        last:4
        before: "WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wMVQyMDowNzozNy45MTciLDFdXQ=="
        after: "WyJjcmVhdGVkX2F0X2Rlc2MiLFsiMjAyMS0xMC0wNVQyMDowNzozNy45MTciLDVdXQ=="
        orderBy:CREATED_AT_DESC) {
      totalCount
      pageInfo { hasNextPage endCursor }
        edges { cursor  node { id title  createdAt  } } } }
     */
    project.last = 3;
    project.after = after(5L);
    project.before = before(1L);
    orderByCreatedAtDesc();
    expectTestConnection().withNodeIds(4L, 3L, 2L);
  }

}