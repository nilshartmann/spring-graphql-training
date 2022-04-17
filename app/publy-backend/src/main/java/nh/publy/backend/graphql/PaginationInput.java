package nh.publy.backend.graphql;

import org.springframework.data.web.ProjectedPayload;

import java.util.Optional;

@ProjectedPayload
public interface PaginationInput {

  Optional<Integer> getFirst();

  Optional<String> getAfter();

  Optional<Integer> getLast();

  Optional<String> getBefore();

}
