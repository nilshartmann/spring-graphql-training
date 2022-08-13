import StoryTeaser from "./StoryTeaser";
import Stack from "./Stack";
import * as React from "react";
import { StoryTeaserFragment } from "../generated/graphql";

type StoryTeaserEdges = Array<{
  node: StoryTeaserFragment;
}>;

type StoryTeaserListProps = {
  edges: StoryTeaserEdges;
  activeTag?: string;
};

export default function StoryTeaserList({
  edges,
  activeTag,
}: StoryTeaserListProps) {
  return (
    <Stack>
      {edges.map(({ node }) => (
        <StoryTeaser key={node.id} story={node} activeTag={activeTag} />
      ))}
    </Stack>
  );
}
