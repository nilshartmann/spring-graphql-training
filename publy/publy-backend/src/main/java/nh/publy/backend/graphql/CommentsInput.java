package nh.publy.backend.graphql;

import org.springframework.data.web.ProjectedPayload;

import java.util.Optional;

@ProjectedPayload
public interface CommentsInput extends PaginationInput {
  Optional<CommentOrderBy> getOrderBy();
}
