package nh.publy.backend.graphql;

import org.springframework.data.web.ProjectedPayload;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@ProjectedPayload
public interface MemberPagination {
  public Optional<@Max(10) Integer> getSize();
  public Optional<@Min(0) Integer> getPage();
}
