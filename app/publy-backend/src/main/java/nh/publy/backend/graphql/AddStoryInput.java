package nh.publy.backend.graphql;

import java.util.List;

public record AddStoryInput(String title, List<String> tags, String body) {
}
