package nh.publy.backend.graphql;

import nh.publy.backend.domain.Comment;

public record AddCommentSuccessResult(Comment newComment) {
}
