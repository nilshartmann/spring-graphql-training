package nh.publy.backend.graphql;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public abstract class AbstractStoryConnectionIntegrationTest {
  final Logger log = LoggerFactory.getLogger(getClass());

  @PersistenceContext
  EntityManager entityManager;

  protected ConnectionTestUtils.DefaultInputProjection project;

  protected StoryOrderBy customOrder = null;
  protected Long withUserId = null;

  @BeforeEach
  void setupInputProjection() {
    project = new ConnectionTestUtils.DefaultInputProjection();
  }

  TestConnection expectTestConnection() {
    StoryFilterCondition condition = withUserId != null ?
      new StoryFilterCondition(StoryConditionField.writtenBy, withUserId) : null;
    StoryConnection storyConnection = new StoryConnection(
      entityManager,
      project,
      Optional.ofNullable(condition),
      Optional.ofNullable(customOrder)
    );

    return new TestConnection(storyConnection);
  }
}
