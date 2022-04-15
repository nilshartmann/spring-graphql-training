package nh.publy.backend.domain;

import java.math.BigInteger;

public record ReactionByType(ReactionType type, BigInteger totalCount,
                             Boolean givenByMe) {

}
