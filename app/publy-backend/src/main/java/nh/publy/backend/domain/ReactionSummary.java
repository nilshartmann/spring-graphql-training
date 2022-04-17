package nh.publy.backend.domain;

import java.math.BigInteger;
import java.util.List;

public record ReactionSummary(List<ReactionByType> reactionsByType) {
  public BigInteger getTotalCount() {
    return reactionsByType.stream()
      .map(ReactionByType::totalCount)
      .reduce(BigInteger.ZERO, BigInteger::add);
  }
}
