package nh.publy.backend.graphql;

import graphql.schema.DataFetchingFieldSelectionSet;
import nh.publy.backend.domain.CommentRepository;
import nh.publy.backend.domain.ReactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NodeGraphQLController {
  private static final Logger log = LoggerFactory.getLogger(NodeGraphQLController.class);

  private final MemberGraphQLController memberGraphQLController;
  private final StoryGraphQLController storyGraphQLController;
  private final CommentRepository commentRepository;
  private final ReactionRepository reactionRepository;

  public NodeGraphQLController(MemberGraphQLController memberGraphQLController, StoryGraphQLController storyGraphQLController, CommentRepository commentRepository, ReactionRepository reactionRepository) {
    this.memberGraphQLController = memberGraphQLController;
    this.storyGraphQLController = storyGraphQLController;
    this.commentRepository = commentRepository;
    this.reactionRepository = reactionRepository;
  }

  @QueryMapping
  public Object node(@Argument NodeId id, DataFetchingFieldSelectionSet dataFetchingFieldSelectionSet) {
    log.info("Resolving node with id {}", id);

    if (!DemoConfig.useComplexNodeIds) {
      log.error("To use the node field, please enable 'useComplexNodeIs'");
      throw new IllegalStateException("To use the node field, please enable 'DemoConfig.useComplexNodeIs'");
    }

    return switch (id.getNodeType()) {
      case member -> memberGraphQLController.member(id);
      case story -> storyGraphQLController.story(id, dataFetchingFieldSelectionSet);
      case comment -> commentRepository.findById(id.getId());
      case reaction -> reactionRepository.findById(id.getReactionId());
    };
  }

}
