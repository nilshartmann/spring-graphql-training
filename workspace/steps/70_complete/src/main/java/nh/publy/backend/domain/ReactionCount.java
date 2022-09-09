package nh.publy.backend.domain;

import java.math.BigInteger;

public interface ReactionCount {
  ReactionType getType();

  BigInteger getTotalCount();
}
