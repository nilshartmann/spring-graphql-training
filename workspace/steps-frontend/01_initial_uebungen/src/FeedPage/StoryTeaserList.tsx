import * as React from "react";
import { StoryTeaserFragment } from "../generated/graphql";
import Stack from "../components/Stack";
import { Link as RouterLink } from "react-router-dom";
import { formatShortDate } from "../utils/formatDateTime";

type StoryTeaserListProps = {
  stories: StoryTeaserFragment[];
};

export default function StoryTeaserList({ stories }: StoryTeaserListProps) {
  return (
    <Stack>
      {stories.map((story) => (
        <div
          key={story.id}
          className={
            "bg-orange-50 rounded-md border border-orange-100 p-4 hover:bg-orange-100 hover:cursor-pointer"
          }
        >
          <Stack orientation={"horizontal"}>
            <div className={"w-24"}>
              <img
                alt={"Profile image of " + story.writtenBy.user?.name}
                src={story.writtenBy.profileImage}
              />
            </div>
            <Stack>
              <div>
                <span
                  className={
                    "text-orange-900 font-light hover:bg-orange-100 hover:underline hover:cursor-pointer"
                  }
                >
                  <RouterLink to={`/u/${story.writtenBy.id}`}>
                    {story.writtenBy.user?.name}
                  </RouterLink>
                </span>

                <div className={" font-extralight text-xs"}>
                  {formatShortDate(story.createdAt)}
                </div>
              </div>
              <div>
                <RouterLink to={`/s/${story.id}`}>
                  <h1
                    className={
                      "text-orange-500 text-3xl font-bold tracking-wide hover:underline hover:decoration-4 hover:decoration-orange-500 hover:text-orange-500"
                    }
                  >
                    {story.title}
                  </h1>
                </RouterLink>
                <p className={"italic"}>{story.excerpt}</p>
              </div>
            </Stack>
          </Stack>
        </div>
      ))}
    </Stack>
  );
}
