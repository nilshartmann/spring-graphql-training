package nh.publy.backend.graphql;

import nh.publy.backend.domain.Story;

import java.util.List;

public record StoryPage(List<Story> stories,
                        Boolean hasPrevPage,
                        Boolean hasNextPage,
                        long totalStories) {
}
