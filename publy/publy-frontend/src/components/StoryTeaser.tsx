import Stack from "./Stack";
import { Link as RouterLink } from "react-router-dom";
import TagLine from "./TagLine";
import { ChatIcon, HeartIcon } from "@heroicons/react/outline";
import * as React from "react";
import { StoryTeaserFragment } from "../generated/graphql";
import { formatShortDate } from "../utils/formatDateTime";

type StoryTeaserProps = {
  story?: StoryTeaserFragment;
  activeTag?: string;
};

export default function StoryTeaser({ story, activeTag }: StoryTeaserProps) {
  if (!story) {
    return <DummyDeprecatedStoryTeaser />;
  }

  return (
    <div
      className={
        "bg-orange-50 rounded-md border border-orange-100 p-4 hover:bg-orange-100 hover:cursor-pointer"
      }
    >
      <Stack orientation={"horizontal"}>
        <div className={"w-24"}>
          <img src={story.writtenBy.profileImageUrl} />
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
            <TagLine activeTag={activeTag} tags={story.tags} />
          </div>

          <Stack orientation={"horizontal"} space={10}>
            <div className={"flex font-light"}>
              <HeartIcon className={"h-5 mr-2"} />
              {story.reactions.totalCount} Reactions
            </div>
            <div className={"flex  font-light"}>
              <ChatIcon className={"h-5 mr-2"} />
              {story.comments.totalCount} Comments
            </div>
          </Stack>
        </Stack>
      </Stack>
    </div>
  );
}

function DummyDeprecatedStoryTeaser() {
  return (
    <div
      className={
        "bg-orange-50 rounded-md border border-orange-100 p-4 hover:bg-orange-100 hover:cursor-pointer"
      }
    >
      <Stack orientation={"horizontal"}>
        <div className={"w-1/12"}>
          <img src={"http://localhost:8080/avatars/avatar_U1.png"} />
        </div>
        <Stack>
          <div>
            <span
              className={
                "text-orange-900 font-light hover:bg-orange-100 hover:underline hover:cursor-pointer"
              }
            >
              <RouterLink to={"/u"}>Zahra Khan</RouterLink>
            </span>

            <div className={" font-extralight text-xs"}>
              Dec 27 (49 mins ago)
            </div>
          </div>
          <div>
            <RouterLink to={"/a"}>
              <h1
                className={
                  "text-orange-500 text-3xl font-bold tracking-wide hover:underline hover:decoration-4 hover:decoration-orange-500 hover:text-orange-500"
                }
              >
                How to Create Aliases on macOS with Terminal
              </h1>
            </RouterLink>
            <TagLine />
          </div>

          <Stack orientation={"horizontal"} space={10}>
            <div className={"flex font-light"}>
              <HeartIcon className={"h-5 mr-2"} />4 Likes
            </div>
            <div className={"flex  font-light"}>
              <ChatIcon className={"h-5 mr-2"} />3 Comments
            </div>
          </Stack>
        </Stack>
      </Stack>
    </div>
  );
}
